package com.fms.bean;

import com.fms.entity.Users;

import com.fms.service.UserServiceLocal;
import com.fms.service.FeedbackServiceLocal;

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

    private int branchId;

 
    // Rating Analysis

    private String ratingLabels;
    private String ratingData;

    // Feedback Trend

    private String trendLabels;
    private String trendData;
    
    private String joiningLabels;
    private String joiningData;

    private String statusLabels;
    private String statusData;
    


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

        loadJoiningTrend();

        loadStatusDistribution();

        loadRatingAnalysis();

        loadFeedbackTrend();
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
    
    private void loadJoiningTrend() {

    StringBuilder labels =
        new StringBuilder();

    StringBuilder data =
        new StringBuilder();

    List<Object[]> result =
        userService
        .getMonthlyStaffJoiningTrend(
            branchId
        );

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

    joiningLabels =
        labels.toString();

    joiningData =
        data.toString();
}
    
    private void loadStatusDistribution() {

    StringBuilder labels =
        new StringBuilder();

    StringBuilder data =
        new StringBuilder();

    List<Object[]> result =
        userService
        .getStaffStatusDistribution(
            branchId
        );

    for(Object[] row : result) {

        labels.append("'")
              .append(row[0])
              .append("',");

        data.append(row[1])
            .append(",");
    }

    statusLabels =
        labels.toString();

    statusData =
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
    
    public String getJoiningLabels() {
        return joiningLabels;
    }

    public String getJoiningData() {
        return joiningData;
    }

    public String getStatusLabels() {
        return statusLabels;
    }

    public String getStatusData() {
        return statusData;
    }
}