package com.fms.bean;

import com.fms.entity.CompanyRegistrationRequests;
import com.fms.service.CompanyServiceLocal;
import com.fms.service.FranchiseServiceLocal;
import com.fms.service.UserServiceLocal;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class AdminCompanyBean implements Serializable {

    @EJB
    private CompanyServiceLocal companyService;
    @EJB
    private UserServiceLocal userService;
    @EJB
    private FranchiseServiceLocal franchiseService;
    
    private String selectedStatus = "ALL";
    private long totalCompanies;
    private long pendingRequests;
    private long totalUsers;
    private long activeFranchises;

    private List<CompanyRegistrationRequests> companies;

    @PostConstruct
    public void init() {

        companies = companyService.getAllRequests();
        totalCompanies = companyService.getTotalCompanies();

        pendingRequests =
                companyService.getPendingRequestCount();

        totalUsers =
                userService.getTotalUsers();

        activeFranchises =
                franchiseService.getActiveFranchiseCount();
    }

    public List<CompanyRegistrationRequests> getCompanies() {
        return companies;
    }
    
    public void filterCompanies(String status) {

        selectedStatus = status;

        if(status.equals("ALL")) {

            companies = companyService.getAllRequests();

        } else {

            companies =
                    companyService.getCompaniesByStatus(status);
        }
    }
    
    public String getSelectedStatus() {
        return selectedStatus;
    }

    public void setSelectedStatus(String selectedStatus) {
        this.selectedStatus = selectedStatus;
    }
    
    public long getTotalCompanies() {
        return totalCompanies;
    }

    public long getPendingRequests() {
        return pendingRequests;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public long getActiveFranchises() {
        return activeFranchises;
    }
}