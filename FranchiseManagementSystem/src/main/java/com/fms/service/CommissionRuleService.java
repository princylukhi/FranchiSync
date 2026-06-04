package com.fms.service;

import com.fms.entity.CommissionRules;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.List;

@Stateless
public class CommissionRuleService
        implements CommissionRuleServiceLocal {

    @PersistenceContext(unitName = "FranchisePU")
    private EntityManager em;

    @Override
    public void addRule(CommissionRules rule) {

        rule.setStatus("ACTIVE");

        em.persist(rule);
    }

    @Override
    public void updateRule(CommissionRules rule) {

        em.merge(rule);
    }

    @Override
    public void deactivateRule(int coid) {

        CommissionRules rule =
                em.find(
                        CommissionRules.class,
                        coid
                );

        if (rule != null) {

            rule.setStatus("INACTIVE");

            em.merge(rule);
        }
    }

    @Override
    public void activateRule(int coid) {

        CommissionRules rule =
                em.find(
                        CommissionRules.class,
                        coid
                );

        if (rule != null) {

            rule.setStatus("ACTIVE");

            em.merge(rule);
        }
    }

    @Override
    public List<CommissionRules> getRulesByCompany(
            int companyId) {

        Query q = em.createQuery(

            "SELECT c FROM CommissionRules c " +
            "WHERE c.cid.cid = :cid " +
            "ORDER BY c.minSales ASC"

        );

        q.setParameter("cid", companyId);

        return q.getResultList();
    }
}