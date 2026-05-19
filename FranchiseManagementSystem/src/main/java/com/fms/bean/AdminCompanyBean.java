package com.fms.bean;

import com.fms.entity.CompanyRegistrationRequests;
import com.fms.service.CompanyServiceLocal;

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
    private String selectedStatus = "ALL";

    private List<CompanyRegistrationRequests> companies;

    @PostConstruct
    public void init() {

        companies = companyService.getAllRequests();
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
}