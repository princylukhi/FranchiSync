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
       if(feedback.getFeedbackType() == null) {

            feedback.setFeedbackType("COMPANY");
        }

        em.persist(feedback);
        
        notificationService.sendNotification(

        "admin@franchisync.com",

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

        return em.createQuery(

            "SELECT f FROM Feedbacks f " +
            "WHERE f.cid.cid = :cid " +
            "AND f.feedbackType <> 'COMPANY' " +
            "ORDER BY f.feedbackDate DESC",

            Feedbacks.class

        )
        .setParameter("cid", companyId)
        .getResultList();
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
    
    @Override
    public List<Feedbacks> getBranchFeedbacksByFranchise(
            int franchiseId) {

        return em.createQuery(

            "SELECT f FROM Feedbacks f " +
            "WHERE f.feedbackType='BRANCH' " +
            "AND f.franchiseId=:fid " +
            "ORDER BY f.feedbackDate DESC",

            Feedbacks.class

        )
        .setParameter("fid", franchiseId)
        .getResultList();
    }
    
    @Override
    public long getTotalBranchFeedbacks(
            int franchiseId) {

        return em.createQuery(

            "SELECT COUNT(f) FROM Feedbacks f " +
            "WHERE f.feedbackType='BRANCH' " +
            "AND f.franchiseId=:fid",

            Long.class

        )
        .setParameter("fid", franchiseId)
        .getSingleResult();
    }
    
    @Override
    public long getNegativeBranchFeedbacks(
            int franchiseId) {

        return em.createQuery(

            "SELECT COUNT(f) FROM Feedbacks f " +
            "WHERE f.feedbackType='BRANCH' " +
            "AND f.franchiseId=:fid " +
            "AND f.rating<=2",

            Long.class

        )
        .setParameter("fid", franchiseId)
        .getSingleResult();
    }
    
    @Override
    public double getAverageBranchRating(
            int franchiseId) {

        Double avg = em.createQuery(

            "SELECT AVG(f.rating) FROM Feedbacks f " +
            "WHERE f.feedbackType='BRANCH' " +
            "AND f.franchiseId=:fid",

            Double.class

        )
        .setParameter("fid", franchiseId)
        .getSingleResult();

        return avg != null ? avg : 0;
    }
    
    @Override
    public void submitFranchiseFeedback(
            Feedbacks feedback,
            int userId,
            int companyId,
            int franchiseId) {

        Users user = em.find(Users.class, userId);

        Companies company = em.find(
                Companies.class,
                companyId);

        feedback.setUid(user);
        feedback.setCid(company);

        feedback.setFeedbackDate(new Date());

        feedback.setFeedbackType("FRANCHISE");

        feedback.setFranchiseId(franchiseId);

        em.persist(feedback);
    }
    
    @Override
    public List<Feedbacks> getStaffFeedbacksByBranch(
            int branchId) {

        return em.createQuery(

            "SELECT f FROM Feedbacks f " +
            "WHERE f.feedbackType = 'STAFF' " +
            "AND f.branchId = :bid " +
            "ORDER BY f.feedbackDate DESC",

            Feedbacks.class

        )
        .setParameter("bid", branchId)
        .getResultList();
    }
    
         @Override
        public long getTotalStaffFeedbacks(
                int branchId) {

            return em.createQuery(

                "SELECT COUNT(f) FROM Feedbacks f " +
                "WHERE f.feedbackType='STAFF' " +
                "AND f.branchId=:bid",

                Long.class

            )
            .setParameter("bid", branchId)
            .getSingleResult();
        }

        
        @Override
        public long getNegativeStaffFeedbacks(
                int branchId) {

            return em.createQuery(

                "SELECT COUNT(f) FROM Feedbacks f " +
                "WHERE f.feedbackType='STAFF' " +
                "AND f.branchId=:bid " +
                "AND f.rating <= 2",

                Long.class

            )
            .setParameter("bid", branchId)
            .getSingleResult();
        }
        
        @Override
        public double getAverageStaffRating(
                int branchId) {

            Double avg = em.createQuery(

                "SELECT AVG(f.rating) FROM Feedbacks f " +
                "WHERE f.feedbackType='STAFF' " +
                "AND f.branchId=:bid",

                Double.class

            )
            .setParameter("bid", branchId)
            .getSingleResult();

            return avg != null ? avg : 0;
        }
        
        @Override
        public void submitBranchFeedback(
                Feedbacks feedback,
                int userId,
                int companyId,
                int branchId,
                int franchiseId) {

            Users user =
                em.find(Users.class, userId);

            Companies company =
                em.find(Companies.class, companyId);

            feedback.setUid(user);

            feedback.setCid(company);

            feedback.setFeedbackDate(new Date());

            feedback.setFeedbackType("BRANCH");

            feedback.setBranchId(branchId);

            feedback.setFranchiseId(franchiseId);

            em.persist(feedback);
        }
        
       @Override
        public List<Object[]> getBranchFeedbackDistribution(
                int franchiseId) {

            return em.createNativeQuery(

                "SELECT b.branch_name, COUNT(f.fid) " +
                "FROM feedbacks f " +
                "JOIN branches b ON f.branch_id = b.bid " +
                "WHERE f.franchise_id = ? " +
                "GROUP BY b.branch_name"

            )
            .setParameter(1, franchiseId)
            .getResultList();
        }
        
        @Override
        public List<Object[]> getMonthlyFeedbackTrend(int branchId) {

            return em.createNativeQuery(

                "SELECT MONTH(feedback_date), COUNT(*) " +
                "FROM feedbacks " +
                "WHERE branch_id = ? " +
                "GROUP BY MONTH(feedback_date) " +
                "ORDER BY MONTH(feedback_date)"

            )
            .setParameter(1, branchId)
            .getResultList();
        }
        
        @Override
        public List<Object[]> getFeedbackCountByType(int branchId) {

            return em.createQuery(

                "SELECT f.feedbackType, COUNT(f) " +
                "FROM Feedbacks f " +
                "WHERE f.branchId = :branchId " +
                "GROUP BY f.feedbackType",

                Object[].class

            )
            .setParameter("branchId", branchId)
            .getResultList();
        }
        
        @Override
        public List<Object[]> getRatingDistribution(int branchId) {

            return em.createQuery(

                "SELECT f.rating, COUNT(f) " +
                "FROM Feedbacks f " +
                "WHERE f.branchId = :branchId " +
                "GROUP BY f.rating " +
                "ORDER BY f.rating",

                Object[].class

            )
            .setParameter("branchId", branchId)
            .getResultList();
        }
}
    
    