package com.majlo.antares.service;

import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class TicketService {

    private final String uploadDir;
    private final String baseUrl;
    private TemplateEngine templateEngine;

    public TicketService(@Value("${app.base-url}") String baseUrl,
                        @Value("${app.pdf-dir:tickets/}") String uploadDir,
                        TemplateEngine templateEngine) {
        this.uploadDir = uploadDir;
        this.baseUrl = baseUrl;
        this.templateEngine = templateEngine;
    }

    public String generateTicketPdf(
            String eventName,
            String seatNumber,
            String seatRow,
            String seatSector,
            String ticketType,
            String price,
            String date
    ) throws IOException  {
        Context context = new Context();
        context.setVariable("eventName", eventName);
        context.setVariable("seatNumber", seatNumber);
        context.setVariable("seatRow", seatRow);
        context.setVariable("seatSector", seatSector);
        context.setVariable("ticketType", ticketType);
        context.setVariable("price", price);
        context.setVariable("date", date);
        String htmlContent = templateEngine.process("ticket-template", context);

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID().toString() + "_ticket.pdf";
        Path filePath = uploadPath.resolve(fileName);

        HtmlConverter.convertToPdf(htmlContent, Files.newOutputStream(filePath));

        return "/api/tickets/files/" + fileName;
    }


    public byte[] getTicket(String filename) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(filename);
        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + filename);
        }
        return Files.readAllBytes(filePath);
    }
}
