package com.fms.service;

import com.fms.entity.CommissionRules;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.math.BigDecimal;

import java.util.List;

@Stateless
public class CommissionRuleService
        implements CommissionRuleServiceLocal {

    @PersistenceContext(unitName = "FranchisePU")
    private EntityManager em;

    @Override
    public String addRule(CommissionRules rule) {

        if(rule.getMinSales()
                .compareTo(rule.getMaxSales()) >= 0) {

            return "Minimum sales must be less than maximum sales";
        }

        Long overlapCount =
            em.createQuery(

                "SELECT COUNT(c) " +
                "FROM CommissionRules c " +
                "WHERE c.cid.cid = :cid " +
                "AND c.status = 'ACTIVE' " +
                "AND (" +
                ":min <= c.maxSales " +
                "AND " +
                ":max >= c.minSales" +
                ")",

                Long.class

            )
            .setParameter("cid",
                    rule.getCid().getCid())
            .setParameter("min",
                    rule.getMinSales())
            .setParameter("max",
                    rule.getMaxSales())
            .getSingleResult();

        if(overlapCount > 0) {

            return "Commission range overlaps with existing rule";
        }

        rule.setStatus("ACTIVE");

        em.persist(rule);

        return "SUCCESS";
    }

    @Override
    public String updateRule(CommissionRules rule) {

        if(rule.getMinSales()
                .compareTo(rule.getMaxSales()) >= 0) {

            return "Minimum sales must be less than maximum sales";
        }

        Long overlapCount =
            em.createQuery(

                "SELECT COUNT(c) " +
                "FROM CommissionRules c " +
                "WHERE c.cid.cid = :cid " +
                "AND c.coid <> :coid " +
                "AND c.status = 'ACTIVE' " +
                "AND (" +
                ":min <= c.maxSales " +
                "AND " +
                ":max >= c.minSales" +
                ")",

                Long.class

            )
            .setParameter("cid",
                    rule.getCid().getCid())
            .setParameter("coid",
                    rule.getCoid())
            .setParameter("min",
                    rule.getMinSales())
            .setParameter("max",
                    rule.getMaxSales())
            .getSingleResult();

        if(overlapCount > 0) {

            return "Commission range overlaps with existing rule";
        }

        em.merge(rule);

        return "SUCCESS";
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
    
    @Override
    public CommissionRules getMatchingRule(
            int companyId,
            BigDecimal salesAmount) {

        List<CommissionRules> rules =
            em.createQuery(

                "SELECT c FROM CommissionRules c " +
                "WHERE c.cid.cid = :cid " +
                "AND c.status = 'ACTIVE' " +
                "AND :sales BETWEEN c.minSales AND c.maxSales",

                CommissionRules.class

            )
            .setParameter("cid", companyId)
            .setParameter("sales", salesAmount)
            .getResultList();

        return rules.isEmpty()
                ? null
                : rules.get(0);
    }
}