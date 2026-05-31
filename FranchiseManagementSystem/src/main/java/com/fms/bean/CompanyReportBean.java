package com.fms.bean;

import com.fms.entity.Users;
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
public class CompanyReportBean implements Serializable {

    @EJB
    private FranchiseServiceLocal franchiseService;

    private int companyId;

    // Franchise Growth Chart
    private String growthLabels;
    private String growthData;

    // Branch Distribution Chart
    private String branchLabels;
    private String branchData;

    // Top Performing Franchise Chart
    private String topLabels;
    private String topData;

    @PostConstruct
    public void init() {

        HttpSession session =
            (HttpSession) FacesContext
            .getCurrentInstance()
            .getExternalContext()
            .getSession(false);

        if (session != null) {

            Users user =
                (Users) session.getAttribute("user");

            if (user != null &&
                user.getCid() != null) {

                companyId =
                    user.getCid().getCid();

                loadFranchiseGrowth();

                loadBranchDistribution();

                loadTopPerformers();
            }
        }
    }

    // ==========================
    // FRANCHISE GROWTH
    // ==========================

    private void loadFranchiseGrowth() {

        StringBuilder labels =
            new StringBuilder();

        StringBuilder data =
            new StringBuilder();

        List<Object[]> result =
            franchiseService
            .getMonthlyFranchiseGrowth(companyId);

        for (Object[] row : result) {

            labels.append("'")
                  .append(row[0])
                  .append("',");

            data.append(row[1])
                .append(",");
        }

        growthLabels =
            labels.toString();

        growthData =
            data.toString();
    }

    // ==========================
    // BRANCH DISTRIBUTION
    // ==========================

    private void loadBranchDistribution() {

        StringBuilder labels =
            new StringBuilder();

        StringBuilder data =
            new StringBuilder();

        List<Object[]> result =
            franchiseService
            .getBranchDistribution(companyId);

        for (Object[] row : result) {

            labels.append("'")
                  .append(row[0])
                  .append("',");

            data.append(row[1])
                .append(",");
        }

        branchLabels =
            labels.toString();

        branchData =
            data.toString();
    }

    // ==========================
    // TOP PERFORMERS
    // ==========================

    private void loadTopPerformers() {

        StringBuilder labels =
            new StringBuilder();

        StringBuilder data =
            new StringBuilder();

        List<Object[]> result =
            franchiseService
            .getTopPerformingFranchises(companyId);

        for (Object[] row : result) {

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

    // ==========================
    // GETTERS
    // ==========================

    public String getGrowthLabels() {
        return growthLabels;
    }

    public String getGrowthData() {
        return growthData;
    }

    public String getBranchLabels() {
        return branchLabels;
    }

    public String getBranchData() {
        return branchData;
    }

    public String getTopLabels() {
        return topLabels;
    }

    public String getTopData() {
        return topData;
    }
}