package com.fms.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;

import com.fms.entity.Branches;

@Stateless
public class BranchService implements BranchServiceLocal {

    @PersistenceContext(unitName = "FranchisePU")
    private EntityManager em;

    @Override
    public void addBranch(Branches branch) {

        branch.setStatus("ACTIVE");

        em.persist(branch);
    }

    @Override
    public void updateBranch(Branches branch) {

        em.merge(branch);
    }

    @Override
    public List<Branches> getBranchesByFranchise(int fid) {

        Query q = em.createQuery(
            "SELECT b FROM Branches b WHERE b.fid.fid = :fid");

        q.setParameter("fid", fid);

        return q.getResultList();
    }

    @Override
    public void activateBranch(int branchId) {

        Branches branch = em.find(Branches.class, branchId);

        branch.setStatus("ACTIVE");

        em.merge(branch);
    }

    @Override
    public void deactivateBranch(int branchId) {

        Branches branch = em.find(Branches.class, branchId);

        branch.setStatus("INACTIVE");

        em.merge(branch);
    }
}