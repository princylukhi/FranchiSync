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
public class AdminRequestBean implements Serializable {

    @EJB
    private CompanyServiceLocal companyService;

    private List<CompanyRegistrationRequests> requests;

    @PostConstruct
    public void init() {

        requests = companyService.getPendingRequests();
    }

    // APPROVE
  public void approve(int id) {

    companyService.approveCompany(id);

    requests = companyService.getPendingRequests();
}

public void reject(int id) {

    companyService.rejectCompany(id);

    requests = companyService.getPendingRequests();
}

    public List<CompanyRegistrationRequests> getRequests() {
        return requests;
    }
}