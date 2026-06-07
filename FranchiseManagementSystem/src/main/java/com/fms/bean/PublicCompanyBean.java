package com.fms.bean;

import com.fms.entity.Companies;
import com.fms.entity.FranchiseRequests;
import com.fms.entity.Users;
import com.fms.service.BillingServiceLocal;

import com.fms.service.CompanyServiceLocal;
import com.fms.service.FranchiseServiceLocal;
import com.fms.service.ProductServiceLocal;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import jakarta.servlet.http.HttpSession;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Named
@ViewScoped
public class PublicCompanyBean
implements Serializable {

    @EJB
    private CompanyServiceLocal companyService;

    @EJB
    private FranchiseServiceLocal franchiseService;

    @EJB
    private ProductServiceLocal productService;
    
    @EJB
    private BillingServiceLocal billingService;

    // EXISTING
    private List<Companies> companies;

    // DASHBOARD DATA
    private int companyId;

    private long totalFranchises;
    private long pendingRequests;
    private long totalProducts;
    
    private String companyName;
    
    private String formattedRevenue;

    private List<FranchiseRequests> recentRequests;

    @PostConstruct
    public void init() {

        // EXISTING
        companies =
            companyService.getApprovedCompanies();

        // SESSION
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
                
                companyName =
                    user.getCid()
                        .getCompanyName();

                loadDashboardData();
            }
        }
    }

    // LOAD DASHBOARD
    public void loadDashboardData() {

        totalFranchises =
            franchiseService
            .getApprovedFranchiseCount(companyId);

        pendingRequests =
            franchiseService
            .getPendingFranchiseCount(companyId);

        totalProducts =
            productService
            .getProductsByCompany(companyId)
            .size();

        recentRequests =
            franchiseService
            .getRecentRequests(companyId);
        
        BigDecimal revenue =
            billingService.getMonthlyRevenueByCompany(
                companyId
            );

        formattedRevenue = formatRevenue(revenue);
    }
    
        private String formatRevenue(
            BigDecimal amount) {

        double value = amount.doubleValue();

        if (value >= 10000000) {
            return String.format(
                "%.1fCr",
                value / 10000000
            );
        }

        if (value >= 100000) {
            return String.format(
                "%.1fL",
                value / 100000
            );
        }

        if (value >= 1000) {
            return String.format(
                "%.1fK",
                value / 1000
            );
        }

        return String.format(
            "%.0f",
            value
        );
    }

    // GETTERS & SETTERS

    public List<Companies> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Companies> companies) {
        this.companies = companies;
    }

    public long getTotalFranchises() {
        return totalFranchises;
    }

    public long getPendingRequests() {
        return pendingRequests;
    }

    public long getTotalProducts() {
        return totalProducts;
    }

    public List<FranchiseRequests> getRecentRequests() {
        return recentRequests;
    }
    
    public String getFormattedRevenue() {
        return formattedRevenue;
    }
    
    public String getCompanyName() {
        return companyName;
    }
}