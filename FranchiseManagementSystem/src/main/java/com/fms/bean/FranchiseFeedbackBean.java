package com.fms.bean;

import com.fms.entity.Feedbacks;
import com.fms.entity.Franchises;
import com.fms.entity.Users;
import com.fms.service.BranchServiceLocal;

import com.fms.service.FeedbackServiceLocal;
import com.fms.service.FranchiseServiceLocal;

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
public class FranchiseFeedbackBean
implements Serializable {

    @EJB
    private FeedbackServiceLocal feedbackService;

    @EJB
    private FranchiseServiceLocal franchiseService;
    
    @EJB
    private BranchServiceLocal branchService;
    
    private Feedbacks feedback =
        new Feedbacks();

    private Users loggedInUser;

    private int companyId;
    
 

    private long totalBranches;

    private int franchiseId;

    private long totalFeedbacks;
    private long negativeFeedbacks;
    private double averageRating;

    private List<Feedbacks> feedbacks;

    @PostConstruct
    public void init() {

        HttpSession session =
            (HttpSession) FacesContext
            .getCurrentInstance()
            .getExternalContext()
            .getSession(false);

        if (session == null) {
            return;
        }

        Users user =
            (Users) session.getAttribute("user");

        if (user == null) {
            return;
        }

        Franchises franchise =
            franchiseService
            .getFranchiseByOwner(user.getUid());

        if (franchise == null) {
            return;
        }

        loggedInUser = user;

        franchiseId =
            franchise.getFid();

        companyId =
            franchise.getCid().getCid();

        loadData();
    }
    public void loadData() {

        totalFeedbacks =
            feedbackService
            .getTotalBranchFeedbacks(franchiseId);

        negativeFeedbacks =
            feedbackService
            .getNegativeBranchFeedbacks(franchiseId);

        averageRating =
            feedbackService
            .getAverageBranchRating(franchiseId);

        feedbacks =
            feedbackService
            .getBranchFeedbacksByFranchise(franchiseId);
        
        totalBranches =
            branchService
            .getTotalBranchesByFranchise(franchiseId);
    }
    
    public void submitCompanyFeedback() {

        feedbackService.submitFranchiseFeedback(

            feedback,

            loggedInUser.getUid(),

            companyId,

            franchiseId
        );

        feedback = new Feedbacks();

        loadData();
    }

    public String getBranchName(int branchId) {

        return branchService
                .getBranchNameById(branchId);
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

    public List<Feedbacks> getFeedbacks() {
        return feedbacks;
    }
    
    public long getTotalBranches() {
        return totalBranches;
    }
    
    public Feedbacks getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedbacks feedback) {
        this.feedback = feedback;
    }
}