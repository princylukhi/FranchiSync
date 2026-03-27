package com.fms.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.Date;
import java.util.List;


import com.fms.entity.CompanyRegistrationRequests;
import com.fms.entity.Companies;

import com.fms.entity.Users;
import jakarta.ejb.EJB;

@Stateless
public class CompanyService implements CompanyServiceLocal {

    @PersistenceContext(unitName = "FranchisePU")
    private EntityManager em;
    
    
    @EJB
    private UserServiceLocal userService;
    @EJB
    private NotificationServiceLocal notificationService;
    

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

        
         // 2️⃣ Create Super Admin (IMPORTANT)
        Users admin = new Users();

        admin.setName("Super Admin");
        admin.setEmail(req.getEmail());
        admin.setPassword("admin123");

        // Call UserService
        userService.createUser(admin, 2, company.getCid(), null);
        
        // 3️⃣ Send Notification (ADD HERE ✅)
        notificationService.sendCompanyApproval(req.getEmail());
    }

    @Override
    public void rejectCompany(int requestId) {

        CompanyRegistrationRequests req =
                em.find(CompanyRegistrationRequests.class, requestId);

        req.setStatus("REJECTED");

        em.merge(req);

      
        notificationService.sendCompanyRejection(req.getEmail());

    }
}