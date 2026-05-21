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

        HttpSession session =
            (HttpSession) FacesContext.getCurrentInstance()
            .getExternalContext()
            .getSession(false);

        Users user =
            (Users) session.getAttribute("user");

        if (user != null && user.getCid() != null) {

            companyId = user.getCid().getCid();

            loadRequests();
        }
    }

    // LOAD REQUESTS
    public void loadRequests() {

        requests =
            franchiseService.getPendingRequests();
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