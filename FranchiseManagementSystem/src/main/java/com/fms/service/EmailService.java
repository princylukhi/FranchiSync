package com.fms.service;

import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.ejb.Stateless;
import jakarta.annotation.Resource;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.io.File;

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
    
    @Override
public void sendEmailWithAttachment(
        String to,
        String subject,
        String messageText,
        String attachmentPath) {

    try {

        MimeMessage message =
                new MimeMessage(mailSession);

        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(to));

        message.setSubject(subject);

        Multipart multipart =
                new MimeMultipart();

        // Email Content

        BodyPart textPart =
                new MimeBodyPart();

        textPart.setText(messageText);

        multipart.addBodyPart(textPart);

        // PDF Attachment

        MimeBodyPart attachmentPart =
                new MimeBodyPart();

        FileDataSource source =
                new FileDataSource(
                        new File(attachmentPath));

        attachmentPart.setDataHandler(
                new DataHandler(source));

        attachmentPart.setFileName(
                source.getName());

        multipart.addBodyPart(
                attachmentPart);

        message.setContent(
                multipart);

        Transport.send(message);

    } catch(Exception e) {

        e.printStackTrace();
    }
}
}