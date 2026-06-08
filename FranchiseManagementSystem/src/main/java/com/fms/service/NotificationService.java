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

    @Override
    public void sendFranchiseApproval(String email) {

        sendNotification(

            email,

            "Franchise Request Approved",

            "Dear Applicant,\n\n"

            + "Congratulations! Your franchise "
            + "request has been approved successfully.\n\n"

            + "You can now access your franchise dashboard "
            + "using the login credentials sent to your email.\n\n"

            + "Welcome to FranchiSync.\n\n"

            + "Regards,\n"
            + "FranchiSync Team",

            "FRANCHISE_APPROVAL"
        );
    }
    
    // Franchise Rejected
    @Override
    public void sendFranchiseRejection(String email) {

        sendNotification(

            email,

            "Franchise Request Rejected",

            "Dear Applicant,\n\n"

            + "We regret to inform you that your "
            + "franchise request has been rejected "
            + "after review.\n\n"

            + "You may apply again later with "
            + "updated information.\n\n"

            + "Regards,\n"
            + "FranchiSync Team",

            "FRANCHISE_REJECTION"
        );
    }

    // 5️⃣ Send Credentials
    @Override
    public void sendCredentials(String email, String password) {

        sendNotification(
        email,

        "FranchiSync Login Credentials",

        "Dear Company Administrator,\n\n"

        + "Welcome to FranchiSync.\n\n"

        + "Your company registration has been approved "
        + "and your company account has been created successfully.\n\n"
                
        + "You can now access the system using the credentials below:\n\n"
        
        + "Login Credentials:\n\n"

        + "Email: " + email + "\n"
        + "Password: " + password + "\n\n"

        + "Please change your password after first login.\n\n"

        + "Regards,\n"
        + "FranchiSync Team",

        "CREDENTIALS"
    );
    }
    
    // Franchise Owner Credentials
    @Override
    public void sendFranchiseCredentials(
            String email,
            String password) {

        sendNotification(

            email,

            "Franchise Login Credentials",

            "Dear Franchise Owner,\n\n"

            + "Congratulations on becoming a franchise partner.\n\n"

            + "Your Franchise Owner account has been created successfully.\n\n"

            + "You can now access the system using the credentials below:\n\n"

            + "Login Credentials:\n\n"

            + "Email: " + email + "\n"
            + "Password: " + password + "\n\n"

            + "Please change your password after first login.\n\n"

            + "Regards,\n"
            + "FranchiSync Team",

            "FRANCHISE_CREDENTIALS"
        );
    }

    // 6️⃣ Get all notifications (for admin panel)
    @Override
    public List<Notifications> getAllNotifications() {

        return em.createQuery(

            "SELECT n FROM Notifications n " +
            "ORDER BY n.sentDate DESC",

            Notifications.class

        ).getResultList();
    }

    // Company Request Received
    @Override
    public void sendCompanyRequestReceivedEmail(
            String email) {

        try {

            String subject =
                    "Company Registration Request Received";

            String message =

                    "Dear Company,\n\n"

                    + "Your company registration request "
                    + "has been received successfully.\n\n"

                    + "Our admin team is currently reviewing "
                    + "your application.\n\n"

                    + "You will receive another email once "
                    + "your request is approved or rejected.\n\n"

                    + "Thank you,\n"
                    + "FranchiSync Team";

            emailService.sendEmail(
                    email,
                    subject,
                    message
            );

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
    
    @Override
    public void sendFranchiseRequestReceivedEmail(
            String email) {

        try {

            String subject =
                    "Franchise Request Received";

            String message =

                    "Dear Applicant,\n\n"

                    + "Your franchise request "
                    + "has been received successfully.\n\n"

                    + "Our team is currently reviewing "
                    + "your application.\n\n"

                    + "You will receive another email once "
                    + "your request is approved or rejected.\n\n"

                    + "Thank you,\n"
                    + "FranchiSync Team";

            emailService.sendEmail(
                    email,
                    subject,
                    message
            );

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    @Override
    public List<Notifications> getNotificationsByEmail(String email) {

        Query q = em.createQuery(

            "SELECT n FROM Notifications n "
          + "WHERE n.recipientEmail = :email "
          + "ORDER BY n.sentDate DESC"

        );

        q.setParameter("email", email);

        return q.getResultList();
    }
    
    @Override
    public void sendStaffCredentials(
            String email,
            String password) {

        sendNotification(

            email,

            "Staff Login Credentials",

            "Dear Staff Member,\n\n"

            + "Your staff account has been created successfully.\n\n"

            + "You can now access the system using the credentials below:\n\n"

            + "Login Credentials:\n\n"

            + "Email: " + email + "\n"
            + "Password: " + password + "\n\n"

            + "Please change your password after first login.\n\n"

            + "Regards,\n"
            + "FranchiSync Team",

            "STAFF_CREDENTIALS"
        );
    }
    
    @Override
    public void sendBranchManagerCredentials(
            String email,
            String password) {

        sendNotification(

            email,

            "Branch Manager Login Credentials",

            "Dear Branch Manager,\n\n"

            + "Your Branch Manager account has been created successfully.\n\n"
            + "You can now access the system using the credentials below:\n\n"

            + "Login Credentials:\n\n"

            + "Email: " + email + "\n"
            + "Password: " + password + "\n\n"

            + "Please change your password after first login.\n\n"

            + "Regards,\n"
            + "FranchiSync Team",

            "BRANCH_MANAGER_CREDENTIALS"
        );
    }
    
    @Override
public void sendFranchiseCredentialsWithCertificate(
        String email,
        String password,
        String certificatePath) {

    String subject =
            "Franchise Login Credentials & Certificate";

    String message =

            "Dear Franchise Owner,\n\n"

            + "Congratulations on becoming an "
            + "authorized franchise partner.\n\n"

            + "Your franchise account has been "
            + "activated successfully.\n\n"
            
            + "You can now access the system using "
            + "the credentials below:\n\n"

            + "Login Credentials:\n\n"

            + "Email: "
            + email
            + "\n"

            + "Password: "
            + password
            + "\n\n"

            + "Your official franchise certificate "
            + "is attached with this email.\n\n"

            + "Regards,\n"
            + "FranchiSync Team";
    
    Notifications n = new Notifications();

    n.setRecipientEmail(email);
    n.setSubject(subject);

    n.setMessage(
        "Your franchise account has been activated successfully. Login credentials and franchise certificate have been sent to your registered email."
    );

    n.setNotificationType("FRANCHISE_CREDENTIALS");

    n.setSentDate(new Date());

    em.persist(n);

    emailService.sendEmailWithAttachment(

            email,

            subject,

            message,

            certificatePath
    );
}
}

       

