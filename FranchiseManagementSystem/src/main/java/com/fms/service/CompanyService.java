package com.fms.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.Date;
import java.util.List;


import com.fms.entity.CompanyRegistrationRequests;
import com.fms.entity.Companies;
import jakarta.ejb.EJB;

@Stateless
public class CompanyService implements CompanyServiceLocal {

    @PersistenceContext(unitName = "FranchisePU")
    private EntityManager em;
    
    @EJB
    private EmailServiceLocal emailService;

    @Override
    public void submitCompanyRequest(CompanyRegistrationRequests request) {

        request.setStatus("PENDING");
        request.setRequestDate(new Date());

        em.persist(request);
    }

    @Override
    public List<CompanyRegistrationRequests> getPendingRequests() {

        Query q = em.createNamedQuery("CompanyRegistrationRequests.findByStatus");

        q.setParameter("status", "PENDING");

        return q.getResultList();
    }

    @Override
    public void approveCompany(int requestId) {

        CompanyRegistrationRequests req =
                em.find(CompanyRegistrationRequests.class, requestId);

        req.setStatus("APPROVED");
        req.setApprovedDate(new Date());

        Companies company = new Companies();

        company.setCompanyName(req.getCompanyName());
        company.setEmail(req.getEmail());
        company.setStatus("ACTIVE");
        company.setCreatedDate(new Date());

        em.persist(company);

        // Send Email
        emailService.sendEmail(
                req.getEmail(),
                "Company Approved",
                "Your company registration has been approved."
        );

    }

    @Override
    public void rejectCompany(int requestId) {

        CompanyRegistrationRequests req =
                em.find(CompanyRegistrationRequests.class, requestId);

        req.setStatus("REJECTED");

        em.merge(req);

        emailService.sendEmail(
                req.getEmail(),
                "Company Registration Rejected",
                "Your company registration request has been rejected."
        );

    }
}