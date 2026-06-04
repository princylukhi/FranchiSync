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
    
    private List<FranchiseRequests> pendingRequests;

    private int companyId;

    private String selectedStatus = "ALL";

    @PostConstruct
    public void init() {

        try {

            HttpSession session =
                (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSession(false);

            if (session == null) {
                return;
            }

            Users user =
                (Users) session.getAttribute("user");

            if (user == null || user.getCid() == null) {
                return;
            }

            companyId = user.getCid().getCid();

            loadRequests();
            loadPendingRequests();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // LOAD ALL
    public void loadRequests() {

        requests =
            franchiseService
            .getRequestsByCompany(companyId);
    }
    
    public void loadPendingRequests() {

            pendingRequests =
                franchiseService
                .getPendingRequests(companyId);
        }

    // FILTER
    public void filterRequests(String status) {

        selectedStatus = status;

        if(status.equals("ALL")) {

            requests =
                franchiseService
                .getRequestsByCompany(companyId);

        } else {

            requests =
                franchiseService
                .getRequestsByStatus(companyId, status);
        }
    }

    // APPROVE
    public void approve(int requestId) {

        franchiseService.approveFranchise(requestId);

        loadRequests();
        loadPendingRequests();
    }

    // REJECT
    public void reject(int requestId) {

        franchiseService.rejectFranchise(requestId);

        loadRequests();
        loadPendingRequests();
    }

    // GETTERS

    public List<FranchiseRequests> getRequests() {
        return requests;
    }

    public String getSelectedStatus() {
        return selectedStatus;
    }
    
    public List<FranchiseRequests> getPendingRequests() {
            return pendingRequests;
        }
}