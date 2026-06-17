package com.fms.bean;

import com.fms.entity.Feedbacks;
import com.fms.entity.Users;
import com.fms.service.FeedbackServiceLocal;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import jakarta.faces.context.FacesContext;

import java.io.Serializable;
import java.util.List;

@Named("companyFeedbackBean")
@ViewScoped
public class CompanyFeedbackBean implements Serializable {

    @EJB
    private FeedbackServiceLocal feedbackService;

    private List<Feedbacks> franchiseFeedbacks;

    private Feedbacks feedback = new Feedbacks();

    private long totalFeedbacks;

    private long negativeFeedbacks;

    private double averageRating;

    private Users loggedInUser;

    @PostConstruct
    public void init() {

        HttpSession session =
            (HttpSession) FacesContext
            .getCurrentInstance()
            .getExternalContext()
            .getSession(false);

        loggedInUser = (Users) session.getAttribute("user");

        loadFeedbacks();
    }

    public void loadFeedbacks() {

        franchiseFeedbacks =
            feedbackService.getFranchiseFeedbacks();

        totalFeedbacks = franchiseFeedbacks.size();

        negativeFeedbacks =
            franchiseFeedbacks.stream()
            .filter(f -> f.getRating() <= 2)
            .count();

        averageRating =
            franchiseFeedbacks.stream()
            .mapToInt(Feedbacks::getRating)
            .average()
            .orElse(0);
    }

    // COMPANY GIVES FEEDBACK TO PLATFORM

    public void submitPlatformFeedback() {

        int userId = loggedInUser.getUid();

        int companyId = loggedInUser.getCid().getCid();

        feedback.setFeedbackType("COMPANY");

        feedbackService.submitFeedback(
            feedback,
            userId,
            companyId
        );

        feedback = new Feedbacks();

        loadFeedbacks();
    }

    // GETTERS & SETTERS

    public List<Feedbacks> getFranchiseFeedbacks() {
        return franchiseFeedbacks;
    }

    public Feedbacks getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedbacks feedback) {
        this.feedback = feedback;
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