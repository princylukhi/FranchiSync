package com.fms.service;

import jakarta.ejb.Stateless;
import jakarta.annotation.Resource;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Stateless
public class EmailService implements EmailServiceLocal {

    @Resource(name = "mail/MyMailSession")
    private Session mailSession;

    @Override
    public void sendEmail(String to, String subject, String messageText) {

        try {

            Message message = new MimeMessage(mailSession);

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to));

            message.setSubject(subject);

            message.setText(messageText);

            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}