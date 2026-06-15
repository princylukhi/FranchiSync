package com.fms.bean;

import com.fms.entity.Franchises;
import com.fms.entity.Users;

import com.fms.service.BranchServiceLocal;
import com.fms.service.FeedbackServiceLocal;
import com.fms.service.FranchiseServiceLocal;
import com.fms.service.RoyaltyServiceLocal;
import com.fms.service.SalesServiceLocal;

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
    
    @EJB
    private SalesServiceLocal salesService;

    @EJB
    private RoyaltyServiceLocal royaltyService;

    private int franchiseId;

    // KPI CARDS

    private long totalBranches;

    private long totalStaff;



    // STAFF DISTRIBUTION

    private String staffLabels;

    private String staffData;

    private String salesLabels;
    private String salesData;

    private String royaltyLabels;
    private String royaltyData;

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

        loadSalesDistribution();

        loadRoyaltyDistribution();

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
    // TOP PERFORMERS
    // ======================

    private void loadTopPerformers() {

        StringBuilder labels =
            new StringBuilder();

        StringBuilder data =
            new StringBuilder();

        List<Object[]> result =
            salesService.getTopRevenueBranches(franchiseId);

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

    private void loadSalesDistribution() {

        StringBuilder labels = new StringBuilder();
        StringBuilder data = new StringBuilder();

        List<Object[]> result =
                salesService.getSalesDistribution(franchiseId);

        for(Object[] row : result) {

            labels.append("'")
                  .append(row[0])
                  .append("',");

            data.append(row[1])
                .append(",");
        }

        salesLabels = labels.toString();
        salesData = data.toString();
    }
    
    private void loadRoyaltyDistribution() {

        StringBuilder labels = new StringBuilder();
        StringBuilder data = new StringBuilder();

        royaltyService
                .getRoyaltyReport(franchiseId)
                .forEach(r -> {

                    labels.append("'")
                          .append(r.getBranchName())
                          .append("',");

                    data.append(r.getRoyaltyAmount())
                        .append(",");
                });

        royaltyLabels = labels.toString();
        royaltyData = data.toString();
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

    public String getStaffLabels() {
        return staffLabels;
    }

    public String getStaffData() {
        return staffData;
    }

    public String getTopLabels() {
        return topLabels;
    }

    public String getTopData() {
        return topData;
    }
    
    public String getSalesLabels() {
        return salesLabels;
    }

    public String getSalesData() {
        return salesData;
    }

    public String getRoyaltyLabels() {
        return royaltyLabels;
    }

    public String getRoyaltyData() {
        return royaltyData;
    }
}