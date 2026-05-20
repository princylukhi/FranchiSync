package com.fms.service;

import com.fms.entity.Notifications;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.ejb.EJB;

import java.util.Date;
import java.util.List;

@Stateless
public class NotificationService implements NotificationServiceLocal {

    @PersistenceContext(unitName = "FranchisePU")
    private EntityManager em;

    @EJB
    private EmailServiceLocal emailService;

    // 1️⃣ Core method: Save + Send Email
    @Override
    public void sendNotification(String email, String subject, String message, String type) {

        // Save notification in DB
        Notifications n = new Notifications();

        n.setRecipientEmail(email);
        n.setSubject(subject);
        n.setMessage(message);
        n.setNotificationType(type);
        n.setSentDate(new Date());

        em.persist(n);

        // Send Email
        emailService.sendEmail(email, subject, message);
    }

    // 2️⃣ Company Approved
    @Override
    public void sendCompanyApproval(String email) {

        sendNotification(
        email,
        "Company Registration Approved",
        
        "Dear Company,\n\n"

        + "Congratulations! Your company registration "
        + "request has been approved successfully.\n\n"

        + "You can now access the FranchiSync platform "
        + "using the credentials sent to your email.\n\n"

        + "Thank you for joining FranchiSync.\n\n"

        + "Regards,\n"
        + "FranchiSync Team",

        "COMPANY_APPROVAL"
    );
    }

    // 3️⃣ Company Rejected
    @Override
    public void sendCompanyRejection(String email) {

        sendNotification(
        email,
        "Company Registration Rejected",

        "Dear Company,\n\n"

        + "We regret to inform you that your company "
        + "registration request has been rejected "
        + "after review.\n\n"

        + "You may submit a new request with valid "
        + "details and required information.\n\n"

        + "For further assistance, please contact support.\n\n"

        + "Regards,\n"
        + "FranchiSync Team",

        "COMPANY_REJECTION"
        );
    }

    // 4️⃣ Franchise Approved
    @Override
    public void sendFranchiseApproval(String email) {

        sendNotification(
                email,
                "Franchise Approved",
                "Your franchise request has been approved.",
                "FRANCHISE_APPROVAL"
        );
    }

    // 5️⃣ Send Credentials
    @Override
    public void sendCredentials(String email, String password) {

        sendNotification(
        email,

        "FranchiSync Login Credentials",

        "Dear Company,\n\n"

        + "Your account has been created successfully.\n\n"

        + "You can now login to FranchiSync using "
        + "the following credentials:\n\n"

        + "Email: " + email + "\n"
        + "Password: " + password + "\n\n"

        + "Please change your password after first login "
        + "for security purposes.\n\n"

        + "Regards,\n"
        + "FranchiSync Team",

        "CREDENTIALS"
    );
    }

    // 6️⃣ Get all notifications (for admin panel)
    @Override
    public List<Notifications> getAllNotifications() {

        Query q = em.createNamedQuery("Notifications.findAll");

        return q.getResultList();
    }

    @Override
public void sendRequestReceivedEmail(String email) {

    try {

        String subject =
                "Registration Request Received";

        String message =

                "Dear Company,\n\n"

                + "Your franchise registration request "
                + "has been received successfully.\n\n"

                + "Our admin team is currently reviewing "
                + "your application.\n\n"

                + "You will receive another email once "
                + "your request is approved or rejected.\n\n"

                + "Thank you,\n"
                + "FranchiSync Team";

        // ONLY SEND EMAIL
        emailService.sendEmail(
                email,
                subject,
                message
        );

    } catch (Exception e) {

        e.printStackTrace();

    }
}
}

       

