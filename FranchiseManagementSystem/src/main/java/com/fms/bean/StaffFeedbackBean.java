package com.fms.bean;

import com.fms.entity.Feedbacks;
import com.fms.entity.Users;

import com.fms.service.FeedbackServiceLocal;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import jakarta.servlet.http.HttpSession;

import java.io.Serializable;

@Named
@ViewScoped
public class StaffFeedbackBean
implements Serializable {

    @EJB
    private FeedbackServiceLocal feedbackService;

    private Feedbacks feedback =
            new Feedbacks();

    private Users loggedInUser;

    private int branchId;
    private int franchiseId;
    private int companyId;

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
    }

    // STAFF → BRANCH

    public void submitBranchFeedback() {

        feedback.setFeedbackType("STAFF");

        feedback.setBranchId(branchId);

        feedback.setFranchiseId(franchiseId);

        feedbackService.submitFeedback(

            feedback,

            loggedInUser.getUid(),

            companyId

        );

        feedback =
            new Feedbacks();
    }

    // GETTERS

    public Feedbacks getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedbacks feedback) {
        this.feedback = feedback;
    }
}