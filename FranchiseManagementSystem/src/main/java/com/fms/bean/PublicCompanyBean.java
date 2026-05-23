package com.fms.bean;

import com.fms.entity.Companies;
import com.fms.service.CompanyServiceLocal;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class PublicCompanyBean
implements Serializable {

    @EJB
    private CompanyServiceLocal companyService;

    private List<Companies> companies;

    @PostConstruct
    public void init() {

        companies =
        companyService.getApprovedCompanies();

    }

    public List<Companies> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Companies> companies) {
        this.companies = companies;
    }
}