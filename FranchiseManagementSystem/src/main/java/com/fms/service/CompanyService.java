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
import com.fms.util.PasswordUtil;
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
        
       
        
        em.flush();

        // SEND EMAIL AFTER SUBMISSION
        notificationService
        .sendCompanyRequestReceivedEmail(
                request.getEmail()
        );
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

        // ✅ FIX 1: Update request status
        req.setStatus("APPROVED");
        req.setApprovedDate(new Date());
        em.merge(req);

        // 🔍 Check if company already exists
        Companies existing = null;

        try {
            existing = (Companies) em.createQuery(
                "SELECT c FROM Companies c WHERE c.companyName = :name")
                .setParameter("name", req.getCompanyName())
                .getSingleResult();
        } catch (Exception e) {
            // no result → ok
        }

        Companies company;

        if (existing != null) {
            company = existing;
        } else {
            company = new Companies();

            company.setCompanyName(req.getCompanyName());
            company.setEmail(req.getEmail());
            company.setContactPerson(req.getContactPerson());
            company.setPhone(req.getPhone());
            company.setBusinessType(req.getBusinessType());
            company.setCity(req.getCity());
            company.setLogo(req.getLogo());
            company.setStatus("ACTIVE");
            company.setCreatedDate(new Date());

            em.persist(company);
            em.flush(); // 🔥 ensures ID is generated
        }

        // ✅ FIX 2: Safety check
        if (company.getCid() == null) {
            throw new RuntimeException("Company ID not generated!");
        }

        // Create Super Admin
        Users admin = new Users();

        admin.setName(req.getContactPerson());
        admin.setEmail(req.getEmail());
        admin.setPassword("admin123");


        
        // SEND APPROVAL MAIL
        notificationService.sendCompanyApproval(
            req.getEmail()
        );

        // CREATE COMPANY ADMIN
        userService.createUser(
            admin,
            2,
            company.getCid(),
            null
        );

        // SEND LOGIN CREDENTIALS
        notificationService.sendCredentials(
            req.getEmail(),
            "admin123"
        );
    }

    @Override
    public void rejectCompany(int requestId) {

        CompanyRegistrationRequests req =
                em.find(CompanyRegistrationRequests.class, requestId);

        req.setStatus("REJECTED");
        

        req.setApprovedDate(new Date());

        em.merge(req);

      
        notificationService.sendCompanyRejection(req.getEmail());

    }
    
    @Override
    public List<Companies> getAllCompanies() {

        return em.createNamedQuery(
                "Companies.findAll",
                Companies.class
        ).getResultList();
    }
    
    @Override
    public List<CompanyRegistrationRequests> getAllRequests() {

        return em.createNamedQuery(
                "CompanyRegistrationRequests.findAll",
                CompanyRegistrationRequests.class
        ).getResultList();
    }
    
    @Override
    public List<CompanyRegistrationRequests>
    getCompaniesByStatus(String status) {

        return em.createQuery(
                "SELECT c FROM CompanyRegistrationRequests c WHERE c.status = :status",
                CompanyRegistrationRequests.class
        )
        .setParameter("status", status)
        .getResultList();
    }
    
    @Override
    public String getBusinessTypeByEmail(String email) {

        try {

            return em.createQuery(
                "SELECT c.businessType FROM CompanyRegistrationRequests c WHERE c.email = :email",
                String.class
            )
            .setParameter("email", email)
            .getSingleResult();

        } catch (Exception e) {

            return "General";
        }
    }
    
    @Override
    public long getTotalCompanies() {

        return em.createQuery(
                "SELECT COUNT(c) FROM Companies c",
                Long.class
        ).getSingleResult();
    }

    @Override
    public long getPendingRequestCount() {

        return em.createQuery(
                "SELECT COUNT(r) FROM CompanyRegistrationRequests r WHERE r.status='PENDING'",
                Long.class
        ).getSingleResult();
    }
    
    @Override
    public List<Companies> getApprovedCompanies() {

        return em.createQuery(

            "SELECT c FROM Companies c "
          + "WHERE c.status = 'ACTIVE' "
          + "ORDER BY c.companyName",

            Companies.class

        ).getResultList();
    }
    
    @Override
    public Companies findCompanyById(int companyId) {

        return em.find(Companies.class, companyId);

    }
    
    @Override
    public List<Object[]> getTopCompaniesByFranchiseCount() {

        return em.createQuery(

            "SELECT c.companyName, COUNT(f) " +
            "FROM Companies c " +
            "LEFT JOIN c.franchisesCollection f " +
            "GROUP BY c.companyName " +
            "ORDER BY COUNT(f) DESC",

            Object[].class

        )
        .setMaxResults(5)
        .getResultList();
    }
    
    @Override
        public List<Object[]> getMonthlyCompanyRegistrations() {

            return em.createNativeQuery(

                "SELECT MONTH(created_date), COUNT(*) " +
                "FROM companies " +
                "GROUP BY MONTH(created_date) " +
                "ORDER BY MONTH(created_date)"

            ).getResultList();
        }
        
        @Override
        public List<Object[]> getWeeklyCompanyRegistrations() {

            return em.createNativeQuery(

                "SELECT WEEK(created_date), COUNT(*) " +
                "FROM companies " +
                "GROUP BY WEEK(created_date) " +
                "ORDER BY WEEK(created_date)"

            ).getResultList();
        }
}