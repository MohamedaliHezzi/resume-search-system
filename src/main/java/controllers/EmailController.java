package controllers;

import model.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import services.EmailSenderService;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
public class EmailController {

    @Autowired
    private EmailSenderService emailSenderService;

    @PostMapping("/sendEmail")
    public void sendEmail(
            @RequestParam("toEmail") String toEmail,
            @RequestParam("subject") String subject,
            @RequestParam("body") String body,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            emailSenderService.sendEmailWithAttachment(toEmail, subject, body, file);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            // handle the exception as needed
        }
    }
}
