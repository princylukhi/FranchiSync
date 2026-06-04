package com.fms.bean;

import com.fms.entity.Feedbacks;
import com.fms.entity.Users;

import com.fms.service.FeedbackServiceLocal;
import com.fms.service.UserServiceLocal;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import jakarta.servlet.http.HttpSession;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class BranchFeedbackBean
implements Serializable {

    @EJB
    private FeedbackServiceLocal feedbackService;

    @EJB
    private UserServiceLocal userService;

    private Users loggedInUser;

    private int branchId;
    private int franchiseId;
    private int companyId;

    private Feedbacks feedback =
            new Feedbacks();

    private List<Feedbacks> feedbacks;

    private long totalFeedbacks;
    private long negativeFeedbacks;
    private long totalStaff;

    private double averageRating;

    @PostConstruct
    public void init() {

        HttpSession session =
            (HttpSession) FacesContext
            .getCurrentInstance()
            .getExternalContext()
            .getSession(false);

        if(session == null) {
            return;
        }

        loggedInUser =
            (Users) session.getAttribute("user");

        if(loggedInUser == null) {
            return;
        }

        if(loggedInUser.getBid() == null) {
            return;
        }

        branchId =
            loggedInUser.getBid().getBid();

        franchiseId =
            loggedInUser
            .getBid()
            .getFid()
            .getFid();

        companyId =
            loggedInUser
            .getBid()
            .getFid()
            .getCid()
            .getCid();

        loadData();
    }

    public void loadData() {

        totalFeedbacks =
            feedbackService
            .getTotalStaffFeedbacks(branchId);

        negativeFeedbacks =
            feedbackService
            .getNegativeStaffFeedbacks(branchId);

        averageRating =
            feedbackService
            .getAverageStaffRating(branchId);

        totalStaff =
            userService
            .getStaffCountByBranch(branchId);

        feedbacks =
            feedbackService
            .getStaffFeedbacksByBranch(branchId);
    }

    // BRANCH -> FRANCHISE

    public void submitFranchiseFeedback() {

        feedbackService.submitBranchFeedback(

            feedback,

            loggedInUser.getUid(),

            companyId,

            branchId,

            franchiseId
        );

        feedback = new Feedbacks();

        loadData();
    }

    // STAFF NAME

    public String getStaffName(int userId) {

        return userService.getUserName(userId);
    }

    // GETTERS

    public Feedbacks getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedbacks feedback) {
        this.feedback = feedback;
    }

    public List<Feedbacks> getFeedbacks() {
        return feedbacks;
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

    public long getTotalStaff() {
        return totalStaff;
    }
}