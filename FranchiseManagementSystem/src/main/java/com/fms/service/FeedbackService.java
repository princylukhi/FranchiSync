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
        feedback.setFeedbackType("COMPANY");

        em.persist(feedback);
        
        notificationService.sendNotification(

        "franchisync@gmail.com",

        "New Feedback Received",

        feedback.getUid().getName()
        + " submitted feedback: "
        + feedback.getMessage(),

        "ADMIN_FEEDBACK"

        );

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
    @Override
    public List<Feedbacks> getCompanyFeedbacks() {

        return em.createQuery(
            "SELECT f FROM Feedbacks f WHERE f.feedbackType = 'COMPANY'",
            Feedbacks.class
        ).getResultList();
    }
    
    @Override
    public List<Feedbacks> getFranchiseFeedbacks() {

        return em.createQuery(
            "SELECT f FROM Feedbacks f WHERE f.feedbackType = 'FRANCHISE'",
            Feedbacks.class
        ).getResultList();
    }
    
    @Override
    public List<Feedbacks> getBranchFeedbacks() {

        return em.createQuery(
            "SELECT f FROM Feedbacks f WHERE f.feedbackType = 'BRANCH'",
            Feedbacks.class
        ).getResultList();
    }
    
    @Override
    public List<Feedbacks> getStaffFeedbacks() {

        return em.createQuery(
            "SELECT f FROM Feedbacks f WHERE f.feedbackType = 'STAFF'",
            Feedbacks.class
        ).getResultList();
    }
    
    @Override
    public long getTotalFeedbacks() {

        return em.createQuery(
            "SELECT COUNT(f) FROM Feedbacks f",
            Long.class
        ).getSingleResult();
    }

        @Override
    public long getNegativeFeedbacks() {

        return em.createQuery(
            "SELECT COUNT(f) FROM Feedbacks f WHERE f.rating <= 2",
            Long.class
        ).getSingleResult();
    }
    @Override
    public double getAverageRating() {

        Double avg = em.createQuery(
            "SELECT AVG(f.rating) FROM Feedbacks f",
            Double.class
        ).getSingleResult();

        return avg != null ? avg : 0;
    }

}
    
    