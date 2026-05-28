package com.fms.bean;

import com.fms.entity.Companies;
import com.fms.entity.FranchiseRequests;
import com.fms.entity.Users;

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

    // EXISTING
    private List<Companies> companies;

    // DASHBOARD DATA
    private int companyId;

    private long totalFranchises;
    private long pendingRequests;
    private long totalProducts;

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
}