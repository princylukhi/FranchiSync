package com.fms.service;

import com.fms.entity.Feedbacks;
import com.fms.entity.Users;
import com.fms.entity.Companies;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.ejb.EJB;

import java.util.Date;
import java.util.List;

@Stateless
public class FeedbackService implements FeedbackServiceLocal {

    @PersistenceContext(unitName = "FranchisePU")
    private EntityManager em;

    @EJB
    private NotificationServiceLocal notificationService;

    // 1️⃣ Submit Feedback
    @Override
    public void submitFeedback(Feedbacks feedback, int userId, int companyId) {

        // ✔ Rating validation (1–5)
        if (feedback.getRating() < 1 || feedback.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

//        Users user = em.find(Users.class, userId);
//        Companies company = em.find(Companies.class, companyId);
//
//        feedback.setUid(user);
//        feedback.setCid(company);
//        feedback.setFeedbackDate(new Date());

        Users user = em.find(Users.class, userId);
        Companies company = em.find(Companies.class, companyId);

        // 🔥 ADD THIS CHECK
        if (user == null) {
            throw new RuntimeException("User NOT FOUND with ID: " + userId);
        }

        if (company == null) {
            throw new RuntimeException("Company NOT FOUND with ID: " + companyId);
        }

        feedback.setUid(user);
        feedback.setCid(company);
        feedback.setFeedbackDate(new Date());

        em.persist(feedback);

        // ✔ Notify company (email + DB log)
        notificationService.sendNotification(
                company.getEmail(),
                "New Feedback Received",
                "Rating: " + feedback.getRating() + "\nMessage: " + feedback.getMessage(),
                "FEEDBACK"
        );
    }

    // 2️⃣ Company-wise Feedback Report
    @Override
    public List<Feedbacks> getFeedbacksByCompany(int companyId) {

        Query q = em.createQuery(
                "SELECT f FROM Feedbacks f WHERE f.cid.cid = :cid"
        );

        q.setParameter("cid", companyId);

        return q.getResultList();
    }

    // 3️⃣ Admin - All Feedbacks
    @Override
    public List<Feedbacks> getAllFeedbacks() {

        Query q = em.createNamedQuery("Feedbacks.findAll");

        return q.getResultList();
    }
}