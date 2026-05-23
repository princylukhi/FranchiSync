package com.fms.bean;

import com.fms.entity.Companies;
import com.fms.entity.FranchiseRequests;
import com.fms.service.CompanyServiceLocal;
import com.fms.service.FranchiseServiceLocal;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@ViewScoped
public class FranchiseApplyBean
implements Serializable {

    private FranchiseRequests request =
            new FranchiseRequests();

    private Companies company;

    private String message;

    @EJB
    private CompanyServiceLocal companyService;

    @EJB
    private FranchiseServiceLocal franchiseService;

    @PostConstruct
    public void init() {

        try {

            String cid =
                FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap()
                .get("companyId");

            if (cid != null) {

                int companyId =
                        Integer.parseInt(cid);

                company =
                    companyService.findCompanyById(companyId);

                request.setCid(company);
            }

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    // SUBMIT REQUEST
    public void submitRequest() {

        try {

            franchiseService.submitFranchiseRequest(request);

            FacesContext.getCurrentInstance()
                    .addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_INFO,
                            "Request Submitted",
                            "Your franchise application is under review."
                    )
            );

            request = new FranchiseRequests();

        } catch (Exception e) {

            FacesContext.getCurrentInstance()
                    .addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Error",
                            e.getMessage()
                    )
            );
        }
    }

    // GETTERS SETTERS

    public FranchiseRequests getRequest() {
        return request;
    }

    public void setRequest(FranchiseRequests request) {
        this.request = request;
    }

    public Companies getCompany() {
        return company;
    }

    public void setCompany(Companies company) {
        this.company = company;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}