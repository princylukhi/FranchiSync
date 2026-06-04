package com.fms.bean;

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
public class FranchiseReportBean
implements Serializable {

    @EJB
    private BranchServiceLocal branchService;

    @EJB
    private FeedbackServiceLocal feedbackService;

    @EJB
    private FranchiseServiceLocal franchiseService;

    private int franchiseId;

    // KPI CARDS

    private long totalBranches;

    private long totalStaff;

    private long totalFeedbacks;

    private double averageRating;

    // STAFF DISTRIBUTION

    private String staffLabels;

    private String staffData;

    // FEEDBACK DISTRIBUTION

    private String feedbackLabels;

    private String feedbackData;

    // TOP PERFORMERS

    private String topLabels;

    private String topData;

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

        Users user =
            (Users) session.getAttribute("user");

        if(user == null) {
            return;
        }

        Franchises franchise =
            franchiseService
            .getFranchiseByOwner(user.getUid());

        if(franchise == null) {
            return;
        }

        franchiseId =
            franchise.getFid();

        loadCards();

        loadStaffDistribution();

        loadFeedbackDistribution();

        loadTopPerformers();
    }

    // ======================
    // KPI CARDS
    // ======================

    private void loadCards() {

        totalBranches =
            branchService
            .getTotalBranchesByFranchise(franchiseId);

        totalStaff =
            branchService
            .getTotalStaffByFranchise(franchiseId);

        totalFeedbacks =
            feedbackService
            .getTotalBranchFeedbacks(franchiseId);

        averageRating =
            feedbackService
            .getAverageBranchRating(franchiseId);
    }

    // ======================
    // STAFF DISTRIBUTION
    // ======================

    private void loadStaffDistribution() {

        StringBuilder labels =
            new StringBuilder();

        StringBuilder data =
            new StringBuilder();

        List<Object[]> result =
            branchService
            .getStaffDistribution(franchiseId);

        for(Object[] row : result) {

            labels.append("'")
                  .append(row[0])
                  .append("',");

            data.append(row[1])
                .append(",");
        }

        staffLabels =
            labels.toString();

        staffData =
            data.toString();
    }

    // ======================
    // FEEDBACK DISTRIBUTION
    // ======================

    private void loadFeedbackDistribution() {

        StringBuilder labels =
            new StringBuilder();

        StringBuilder data =
            new StringBuilder();

        List<Object[]> result =
            feedbackService
            .getBranchFeedbackDistribution(franchiseId);

        for(Object[] row : result) {

            labels.append("'")
                  .append(row[0])
                  .append("',");

            data.append(row[1])
                .append(",");
        }

        feedbackLabels =
            labels.toString();

        feedbackData =
            data.toString();
    }

    // ======================
    // TOP PERFORMERS
    // ======================

    private void loadTopPerformers() {

        StringBuilder labels =
            new StringBuilder();

        StringBuilder data =
            new StringBuilder();

        List<Object[]> result =
            branchService
            .getTopPerformingBranches(franchiseId);

        for(Object[] row : result) {

            labels.append("'")
                  .append(row[0])
                  .append("',");

            data.append(row[1])
                .append(",");
        }

        topLabels =
            labels.toString();

        topData =
            data.toString();
    }

    // ======================
    // GETTERS
    // ======================

    public long getTotalBranches() {
        return totalBranches;
    }

    public long getTotalStaff() {
        return totalStaff;
    }

    public long getTotalFeedbacks() {
        return totalFeedbacks;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public String getStaffLabels() {
        return staffLabels;
    }

    public String getStaffData() {
        return staffData;
    }

    public String getFeedbackLabels() {
        return feedbackLabels;
    }

    public String getFeedbackData() {
        return feedbackData;
    }

    public String getTopLabels() {
        return topLabels;
    }

    public String getTopData() {
        return topData;
    }
}