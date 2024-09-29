package com.majlo.antares.controller;

import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class TicketController {
    @Value("${pdf.directory}")
    private String pdfDirectory;

    @Autowired
    private TemplateEngine templateEngine;

    @GetMapping("/generate-ticket/{userName}/{eventName}/{seatNumber}/{price}/{date}")
    public String generateTicket(
            @PathVariable String userName,
            @PathVariable String eventName,
            @PathVariable String seatNumber,
            @PathVariable String price,
            @PathVariable String date,
            Model model
    ) throws IOException {

        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("eventName", eventName);
        context.setVariable("seatNumber", seatNumber);
        context.setVariable("price", price);
        context.setVariable("date", date);
        String htmlContent = templateEngine.process("ticket-template", context);

        String fileName = userName + "_ticket.pdf";
        String filePath = pdfDirectory + fileName;
        HtmlConverter.convertToPdf(htmlContent, new FileOutputStream(filePath));

        //TODO: Fix: Somehow return the file to the user
//        return new RedirectView("/tickets/" + fileName);
        String ticketUrl = "/tickets/" + fileName;
        return ticketUrl;
    }

    @GetMapping("/tickets/{fileName}")
    public Resource getTicket(@PathVariable String fileName) throws IOException {
        Path path = Paths.get(pdfDirectory + fileName);
        return new UrlResource(path.toUri());
    }
}
