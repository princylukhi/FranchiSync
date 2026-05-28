package com.fms.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.Date;
import java.util.List;

import com.fms.entity.FranchiseRequests;
import com.fms.entity.Franchises;
import com.fms.entity.Companies;
import com.fms.entity.Users;
import jakarta.ejb.EJB;

@Stateless
public class FranchiseService implements FranchiseServiceLocal {

    @PersistenceContext(unitName = "FranchisePU")
    private EntityManager em;
    
    @EJB
    private NotificationServiceLocal notificationService;
    
    @EJB
    private UserServiceLocal userService;

    @Override
    public void submitFranchiseRequest(FranchiseRequests request) {

        request.setStatus("PENDING");
        request.setRequestDate(new Date());

        em.persist(request);
        
     
       
       // SEND CONFIRMATION EMAIL
       notificationService
        .sendFranchiseRequestReceivedEmail(
                request.getEmail()
        );
    }

    @Override
    public List<FranchiseRequests> getPendingRequests(int companyId) {

        return em.createQuery(
            "SELECT f FROM FranchiseRequests f " +
            "WHERE f.cid.cid = :cid " +
            "AND f.status = 'PENDING' " +
            "ORDER BY f.requestDate DESC",
            FranchiseRequests.class
        )
        .setParameter("cid", companyId)
        .getResultList();
    }

    @Override
    public void approveFranchise(int requestId) {

        FranchiseRequests req =
                em.find(FranchiseRequests.class, requestId);

        req.setStatus("APPROVED");
        req.setApprovedDate(new Date());

        em.merge(req);

        Companies company = req.getCid();

        // CREATE FRANCHISE OWNER USER
        Users owner = new Users();

        owner.setName(req.getOwnerName());
        owner.setEmail(req.getEmail());
        owner.setPassword("owner123");

        // ROLE ID = 3 (FRANCHISE_OWNER)
        userService.createUser(
                owner,
                3,
                company.getCid(),
                null
        );

        // FETCH SAVED USER
        Users savedOwner =
                userService.findUserByEmail(req.getEmail());

        // CREATE FRANCHISE
        Franchises franchise =
                new Franchises();

        franchise.setCid(company);
        franchise.setOwnerUserId(savedOwner);
        franchise.setStatus("ACTIVE");
        franchise.setCreatedDate(new Date());

        em.persist(franchise);

        // SEND APPROVAL MAIL
        notificationService.sendFranchiseApproval(
                req.getEmail()
        );

        // SEND LOGIN CREDENTIALS
        notificationService.sendFranchiseCredentials(
                req.getEmail(),
                "owner123"
        );
    }

    @Override
    public void rejectFranchise(int requestId) {

        FranchiseRequests req =
                em.find(FranchiseRequests.class, requestId);

        req.setStatus("REJECTED");

        em.merge(req);
        
        notificationService.sendFranchiseRejection(
            req.getEmail()
        );
        
    }

    @Override
    public List<Franchises> getFranchisesByCompany(int cid) {

        Query q = em.createQuery(
            "SELECT f FROM Franchises f WHERE f.cid.cid = :cid");

        q.setParameter("cid", cid);

        return q.getResultList();
    }
    
    @Override
    public long getActiveFranchiseCount() {

        return em.createQuery(
                "SELECT COUNT(f) FROM Franchises f WHERE f.status='ACTIVE'",
                Long.class
        ).getSingleResult();
    }
    
    @Override
        public List<FranchiseRequests>
        getRequestsByCompany(int companyId) {

            return em.createQuery(
                "SELECT f FROM FranchiseRequests f " +
                "WHERE f.cid.cid = :cid " +
                "ORDER BY f.requestDate DESC",
                FranchiseRequests.class
            )
            .setParameter("cid", companyId)
            .getResultList();
        }
}