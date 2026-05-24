package com.fms.bean;

import com.fms.entity.FranchiseRequests;
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
public class CompanyFranchiseBean implements Serializable {

    @EJB
    private FranchiseServiceLocal franchiseService;

    private List<FranchiseRequests> requests;

    private int companyId;

  @PostConstruct
public void init() {

    try {

        HttpSession session =
            (HttpSession) FacesContext.getCurrentInstance()
            .getExternalContext()
            .getSession(false);

        // SESSION NULL CHECK
        if (session == null) {
            return;
        }

        Users user =
            (Users) session.getAttribute("user");

        // USER NULL CHECK
        if (user == null) {
            return;
        }

        // COMPANY NULL CHECK
        if (user.getCid() == null) {
            return;
        }

        companyId = user.getCid().getCid();

        loadRequests();

    } catch (Exception e) {

        e.printStackTrace();
    }
}

    // LOAD REQUESTS
    public void loadRequests() {

        requests =
            franchiseService
            .getRequestsByCompany(companyId);
    }

    // APPROVE
    public void approve(int requestId) {

        // TEMP USER ID
        franchiseService.approveFranchise(
            requestId,
            1
        );

        loadRequests();
    }

    // REJECT
    public void reject(int requestId) {

        franchiseService.rejectFranchise(requestId);

        loadRequests();
    }

    // GETTERS / SETTERS

    public List<FranchiseRequests> getRequests() {
        return requests;
    }

    public void setRequests(List<FranchiseRequests> requests) {
        this.requests = requests;
    }
}