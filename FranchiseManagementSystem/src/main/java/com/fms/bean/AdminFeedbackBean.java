package com.fms.bean;

import com.fms.entity.Feedbacks;
import com.fms.service.FeedbackServiceLocal;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class AdminFeedbackBean implements Serializable {

    @EJB
    private FeedbackServiceLocal feedbackService;

    private List<Feedbacks> companyFeedbacks;
    private List<Feedbacks> franchiseFeedbacks;
    private List<Feedbacks> branchFeedbacks;
    private List<Feedbacks> staffFeedbacks;

    private long totalFeedbacks;
    private long negativeFeedbacks;
    private double averageRating;

    @PostConstruct
    public void init() {

        companyFeedbacks = feedbackService.getCompanyFeedbacks();

        franchiseFeedbacks = feedbackService.getFranchiseFeedbacks();

        branchFeedbacks = feedbackService.getBranchFeedbacks();

        staffFeedbacks = feedbackService.getStaffFeedbacks();

        totalFeedbacks = feedbackService.getTotalFeedbacks();

        negativeFeedbacks = feedbackService.getNegativeFeedbacks();

        averageRating = feedbackService.getAverageRating();
    }

    public List<Feedbacks> getCompanyFeedbacks() {
        return companyFeedbacks;
    }

    public List<Feedbacks> getFranchiseFeedbacks() {
        return franchiseFeedbacks;
    }

    public List<Feedbacks> getBranchFeedbacks() {
        return branchFeedbacks;
    }

    public List<Feedbacks> getStaffFeedbacks() {
        return staffFeedbacks;
    }

    public long getTotalFeedbacks() {
        return totalFeedbacks;
    }

    public long getNegativeFeedbacks() {
        return negativeFeedbacks;
    }

    public double getAverageRating() {
        return averageRating;
    }
}