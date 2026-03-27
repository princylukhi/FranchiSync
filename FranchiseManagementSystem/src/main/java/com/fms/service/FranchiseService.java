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

    @Override
    public void submitFranchiseRequest(FranchiseRequests request) {

        request.setStatus("PENDING");
        request.setRequestDate(new Date());

        em.persist(request);
    }

    @Override
    public List<FranchiseRequests> getPendingRequests() {

        Query q = em.createNamedQuery("FranchiseRequests.findByStatus");

        q.setParameter("status", "PENDING");

        return q.getResultList();
    }

    @Override
    public void approveFranchise(int requestId, int ownerUserId) {

        FranchiseRequests req =
                em.find(FranchiseRequests.class, requestId);

        req.setStatus("APPROVED");
        req.setApprovedDate(new Date());

        Companies company = req.getCid();

        Users owner = em.find(Users.class, ownerUserId);

        Franchises franchise = new Franchises();

        franchise.setCid(company);
        franchise.setOwnerUserId(owner);
        franchise.setStatus("ACTIVE");
        franchise.setCreatedDate(new Date());

        em.persist(franchise);
        
        notificationService.sendFranchiseApproval(owner.getEmail());
    }

    @Override
    public void rejectFranchise(int requestId) {

        FranchiseRequests req =
                em.find(FranchiseRequests.class, requestId);

        req.setStatus("REJECTED");

        em.merge(req);
    }

    @Override
    public List<Franchises> getFranchisesByCompany(int cid) {

        Query q = em.createQuery(
            "SELECT f FROM Franchises f WHERE f.cid.cid = :cid");

        q.setParameter("cid", cid);

        return q.getResultList();
    }
}