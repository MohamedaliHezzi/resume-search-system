package model;

import org.springframework.web.multipart.MultipartFile;

public class EmailRequest {
    private String toEmail;
    private String subject;
    private String body;
    private MultipartFile file;

    // Default constructor
    public EmailRequest() {}

    // Getters and setters
    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
