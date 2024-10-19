package com.javahunter.scm.services;

public interface EmailService {

    void sendEmail(String to, String subject, String body);
    void sendEmailWithHtml(String to, String subject, String body);
    void sendEmailWithAttachment(String to, String subject, String body);
}
