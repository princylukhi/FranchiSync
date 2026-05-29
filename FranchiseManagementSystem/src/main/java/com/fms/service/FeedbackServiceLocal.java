/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package com.fms.service;

import com.fms.entity.Feedbacks;
import jakarta.ejb.Local;
import java.util.List;

/**
 *
 * @author Princy Lukhi
 */
@Local
public interface FeedbackServiceLocal {
    
      // Submit feedback
    public void submitFeedback(Feedbacks feedback, int userId, int companyId);

    // Get company-wise feedback report
    public List<Feedbacks> getFeedbacksByCompany(int companyId);

    // Get all feedbacks (admin)
    public List<Feedbacks> getAllFeedbacks();
    
    public List<Feedbacks> getCompanyFeedbacks();

    public List<Feedbacks> getFranchiseFeedbacks();

    public List<Feedbacks> getBranchFeedbacks();

    public List<Feedbacks> getStaffFeedbacks();

    public long getTotalFeedbacks();

    public long getNegativeFeedbacks();

    public double getAverageRating();
    
    List<Feedbacks> getBranchFeedbacksByFranchise(int franchiseId);

    long getTotalBranchFeedbacks(int franchiseId);

    long getNegativeBranchFeedbacks(int franchiseId);

    double getAverageBranchRating(int franchiseId);
    
    public void submitFranchiseFeedback(
        Feedbacks feedback,
        int userId,
        int companyId,
        int franchiseId);
    
    List<Feedbacks> getStaffFeedbacksByBranch(int branchId);

    long getTotalStaffFeedbacks(int branchId);

    long getNegativeStaffFeedbacks(int branchId);

    double getAverageStaffRating(int branchId);

    void submitBranchFeedback(
            Feedbacks feedback,
            int userId,
            int companyId,
            int branchId,
            int franchiseId);
    
    
    
}
