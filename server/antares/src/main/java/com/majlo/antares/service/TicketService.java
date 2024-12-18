package com.majlo.antares.service;

import com.google.zxing.WriterException;
import com.itextpdf.html2pdf.HtmlConverter;
import com.majlo.antares.model.User;
import com.majlo.antares.model.events.Event;
import com.majlo.antares.model.transaction.Ticket;
import com.majlo.antares.repository.transaction.TicketRepository;
import com.majlo.antares.util.QrCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Service
public class TicketService {

    private final String uploadDir;
    private final String baseUrl;
    private final TicketRepository ticketRepository;
    private TemplateEngine templateEngine;
    private final QrCodeGenerator qrCodeGenerator;

    public TicketService(@Value("${app.base-url}") String baseUrl,
                         @Value("${app.pdf-dir:tickets/}") String uploadDir,
                         TemplateEngine templateEngine,
                         QrCodeGenerator qrCodeGenerator, TicketRepository ticketRepository) {
        this.uploadDir = uploadDir;
        this.baseUrl = baseUrl;
        this.templateEngine = templateEngine;
        this.qrCodeGenerator = qrCodeGenerator;
        this.ticketRepository = ticketRepository;
    }

    public Ticket generateTicketPdf(
            String eventName,
            String seatNumber,
            String seatRow,
            String seatSector,
            String ticketType,
            String price,
            String date,
            User user,
            Event event
    ) throws IOException, WriterException {
        Ticket ticket = new Ticket();
        ticket.setTicketOwner(user);
        ticket.setEvent(event);
        ticket.setIsValidated(false);
        ticket = ticketRepository.save(ticket);

        String uuid = UUID.randomUUID().toString();
        String qrCodeData = uuid + "_" + ticket.getId();
        byte[] qrCodeImage = qrCodeGenerator.generateQrCode(qrCodeData, 300, 300);
        String qrCodeBase64 = Base64.getEncoder().encodeToString(qrCodeImage);


        Context context = new Context();
        context.setVariable("eventName", eventName);
        context.setVariable("seatNumber", seatNumber);
        context.setVariable("seatRow", seatRow);
        context.setVariable("seatSector", seatSector);
        context.setVariable("ticketType", ticketType);
        context.setVariable("price", price);
        context.setVariable("date", date);
        context.setVariable("qrCodeBase64", "data:image/png;base64," + qrCodeBase64);
        String htmlContent = templateEngine.process("ticket-template", context);

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID().toString() + "_ticket.pdf";
        Path filePath = uploadPath.resolve(fileName);

        HtmlConverter.convertToPdf(htmlContent, Files.newOutputStream(filePath));

        ticket.setTicketPdfLink("/api/tickets/files/" + fileName);
        ticket.setValidationUuid(uuid);
        ticketRepository.save(ticket);

        return ticket;
    }


    public byte[] getTicket(String filename) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(filename);
        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + filename);
        }
        return Files.readAllBytes(filePath);
    }

    public boolean validateTicket(String qrCodeData) {
        String[] parts = qrCodeData.split("_");

        if (parts.length != 2) {
            return false;
        }

        String uuid = parts[0];
        String ticketId = parts[1];

        Ticket ticket = ticketRepository.findById(Long.valueOf(ticketId)).orElse(null);

        if (ticket == null) {
            return false;
        }

        if (!ticket.getIsValidated() || !ticket.getValidationUuid().equals(uuid)) {
            return false;
        }

        ticket.setIsValidated(true);
        ticketRepository.save(ticket);

        return true;
    }
}
