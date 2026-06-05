package com.fms.bean;

import com.fms.entity.Users;

import com.fms.service.UserServiceLocal;
import com.fms.service.FeedbackServiceLocal;
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
public class BranchReportBean implements Serializable {

    @EJB
    private UserServiceLocal userService;

    @EJB
    private FeedbackServiceLocal feedbackService;
    
    @EJB
    private SalesServiceLocal salesService;

    private int branchId;

 
    // Rating Analysis

    private String ratingLabels;
    private String ratingData;

    // Feedback Trend

    private String trendLabels;
    private String trendData;
    
    private String salesLabels;
    private String salesData;

    private String statusLabels;
    private String statusData;
    
    private String paymentLabels;
    private String paymentData;
    


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

        if (user == null ||
            user.getBid() == null) {
            return;
        }

        branchId =
            user.getBid().getBid();

        loadSalesTrend();

        loadStatusDistribution();

        loadRatingAnalysis();

        loadFeedbackTrend();
        
        loadPaymentDistribution();
    }

  

    // =========================
    // STAFF RATINGS
    // =========================

    private void loadRatingAnalysis() {

        StringBuilder labels =
            new StringBuilder();

        StringBuilder data =
            new StringBuilder();

        List<Object[]> result =
            feedbackService
            .getRatingDistribution(branchId);

        for(Object[] row : result) {

            labels.append("'")
                  .append(row[0])
                  .append(" Star',");

            data.append(row[1])
                .append(",");
        }

        ratingLabels =
            labels.toString();

        ratingData =
            data.toString();
        
        System.out.println("Rating Labels = " + ratingLabels);
        System.out.println("Rating Data = " + ratingData);
    }

    // =========================
    // FEEDBACK TREND
    // =========================

    private void loadFeedbackTrend() {

        StringBuilder labels =
            new StringBuilder();

        StringBuilder data =
            new StringBuilder();

        List<Object[]> result =
            feedbackService
            .getMonthlyFeedbackTrend(branchId);

        String[] months = {
            "",
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec"
        };

        for (Object[] row : result) {

            labels.append("'")
                  .append(
                      months[
                          Integer.parseInt(
                              row[0].toString()
                          )
                      ]
                  )
                  .append("',");

            data.append(row[1])
                .append(",");
        }

        trendLabels =
            labels.toString();

        trendData =
            data.toString();
        
        
        System.out.println("Trend Labels = " + trendLabels);
        System.out.println("Trend Data = " + trendData);
    }
    
    private void loadSalesTrend() {

        StringBuilder labels =
            new StringBuilder();

        StringBuilder data =
            new StringBuilder();

        List<Object[]> result =
            salesService.getMonthlySalesTrend(branchId);

        String[] months = {

            "",
            "Jan","Feb","Mar","Apr",
            "May","Jun","Jul","Aug",
            "Sep","Oct","Nov","Dec"
        };

        for(Object[] row : result) {

            labels.append("'")
                  .append(
                      months[
                          Integer.parseInt(
                              row[0].toString()
                          )
                      ]
                  )
                  .append("',");

            data.append(row[1])
                .append(",");
        }

        salesLabels =
            labels.toString();

        salesData =
            data.toString();
    }
    
    private void loadStatusDistribution() {

    int active = 0;
    int inactive = 0;

    List<Object[]> result =
        userService.getStaffStatusDistribution(branchId);

    for(Object[] row : result) {

        String status =
            row[0].toString();

        int count =
            Integer.parseInt(
                row[1].toString()
            );

        if(status.equalsIgnoreCase("ACTIVE")) {

            active = count;

        } else if(status.equalsIgnoreCase("INACTIVE")) {

            inactive = count;
        }
    }

    statusLabels =
        "'ACTIVE','INACTIVE'";

    statusData =
        active + "," + inactive;
}
    
    private void loadPaymentDistribution() {

    StringBuilder labels =
        new StringBuilder();

    StringBuilder data =
        new StringBuilder();

    List<Object[]> result =
        salesService.getPaymentModeDistribution(
            branchId
        );

    for(Object[] row : result) {

        labels.append("'")
              .append(row[0])
              .append("',");

        data.append(row[1])
            .append(",");
    }

    paymentLabels =
        labels.toString();

    paymentData =
        data.toString();
}

    // =========================
    // GETTERS
    // =========================

 

    public String getRatingLabels() {
        return ratingLabels;
    }

    public String getRatingData() {
        return ratingData;
    }

    public String getTrendLabels() {
        return trendLabels;
    }

    public String getTrendData() {
        return trendData;
    }
    
    public String getSalesLabels() {
        return salesLabels;
    }

    public String getSalesData() {
        return salesData;
    }

    public String getStatusLabels() {
        return statusLabels;
    }

    public String getStatusData() {
        return statusData;
    }
    
    public String getPaymentLabels() {
        return paymentLabels;
    }

    public String getPaymentData() {
        return paymentData;
    }
}