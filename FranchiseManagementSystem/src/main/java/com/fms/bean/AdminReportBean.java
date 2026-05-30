package com.fms.bean;

import com.fms.service.AdminReportServiceLocal;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class AdminReportBean implements Serializable {

    @EJB
    private AdminReportServiceLocal reportService;

    private String roleLabels;
    private String roleData;

    private String feedbackLabels;
    private String feedbackData;
    
    private String companyLabels;
    private String companyData;

    private String registrationLabels;

    private String companyRegistrationData;
    private String franchiseRegistrationData;
    private String userRegistrationData;

  @PostConstruct
    public void init() {

        loadRoleChart();

        loadFeedbackChart();

        loadTopCompaniesChart();

        loadRegistrationChart();
    }

    // ==========================
    // ROLE DISTRIBUTION
    // ==========================

    private void loadRoleChart() {

        roleLabels =
            "'System Admin','Company Admin','Franchise Owner','Branch Manager','Staff'";

        roleData =

            reportService.getSystemAdminCount() + "," +

            reportService.getCompanyAdminCount() + "," +

            reportService.getFranchiseOwnerCount() + "," +

            reportService.getBranchManagerCount() + "," +

            reportService.getStaffCount();
    }

    // ==========================
    // FEEDBACK ANALYSIS
    // ==========================

    private void loadFeedbackChart() {

        feedbackLabels =
            "'1 Star','2 Star','3 Star','4 Star','5 Star'";

        feedbackData =

            reportService.getRatingCount(1) + "," +

            reportService.getRatingCount(2) + "," +

            reportService.getRatingCount(3) + "," +

            reportService.getRatingCount(4) + "," +

            reportService.getRatingCount(5);
    }
    
        private void loadTopCompaniesChart() {

        List<Object[]> companies =
            reportService.getTopCompaniesByFranchiseCount();

        StringBuilder labels =
            new StringBuilder();

        StringBuilder data =
            new StringBuilder();

        for (int i = 0; i < companies.size(); i++) {

            Object[] row = companies.get(i);

            String companyName =
                row[0].toString()
                      .replace("'", "\\'");

            labels.append("'")
                  .append(companyName)
                  .append("'");

            data.append(
                ((Number) row[1]).intValue()
            );

            if (i < companies.size() - 1) {

                labels.append(",");
                data.append(",");
            }
        }

        companyLabels = labels.toString();
        companyData = data.toString();
    }
    
    
    private void loadRegistrationChart() {

        registrationLabels =
            "'Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'";

        int[] companies =
            new int[12];

        int[] franchises =
            new int[12];

        int[] users =
            new int[12];

        for(Object[] row :
            reportService.getMonthlyCompanyRegistrations()) {

            int month =
                ((Number) row[0]).intValue();

            companies[month - 1] =
                ((Number) row[1]).intValue();
        }

        for(Object[] row :
            reportService.getMonthlyFranchiseRegistrations()) {

            int month =
                ((Number) row[0]).intValue();

            franchises[month - 1] =
                ((Number) row[1]).intValue();
        }

        for(Object[] row :
            reportService.getMonthlyUserRegistrations()) {

            int month =
                ((Number) row[0]).intValue();

            users[month - 1] =
                ((Number) row[1]).intValue();
        }

        companyRegistrationData =
            arrayToString(companies);

        franchiseRegistrationData =
            arrayToString(franchises);

        userRegistrationData =
            arrayToString(users);
    }
    
    private String arrayToString(int[] arr) {

        StringBuilder sb =
            new StringBuilder();

        for(int value : arr) {

            sb.append(value)
              .append(",");
        }

        return sb.toString();
    }

    // ==========================
    // GETTERS
    // ==========================

    public String getRoleLabels() {
        return roleLabels;
    }

    public String getRoleData() {
        return roleData;
    }

    public String getFeedbackLabels() {
        return feedbackLabels;
    }

    public String getFeedbackData() {
        return feedbackData;
    }
    
    public String getCompanyLabels() {
        return companyLabels;
    }

    public String getCompanyData() {
        return companyData;
    }

    public String getRegistrationLabels() {
        return registrationLabels;
    }

    public String getCompanyRegistrationData() {
        return companyRegistrationData;
    }

    public String getFranchiseRegistrationData() {
        return franchiseRegistrationData;
    }

    public String getUserRegistrationData() {
        return userRegistrationData;
    }
}