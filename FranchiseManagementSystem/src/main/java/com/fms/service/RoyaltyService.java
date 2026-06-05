package com.fms.service;

import com.fms.dto.RoyaltyRow;
import com.fms.entity.Branches;
import com.fms.entity.CommissionRules;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class RoyaltyService
implements RoyaltyServiceLocal {

    @PersistenceContext(unitName = "FranchisePU")
    private EntityManager em;

    @EJB
    private CommissionRuleServiceLocal
            commissionRuleService;

    @Override
    public List<RoyaltyRow> getRoyaltyReport(
            int franchiseId) {

        List<RoyaltyRow> rows =
                new ArrayList<>();

        List<Branches> branches =
            em.createQuery(

                "SELECT b FROM Branches b " +
                "WHERE b.fid.fid = :fid",

                Branches.class

            )
            .setParameter("fid", franchiseId)
            .getResultList();

        for(Branches branch : branches) {

            BigDecimal salesAmount =
                em.createQuery(

                    "SELECT SUM(s.totalAmount) " +
                    "FROM Sales s " +
                    "WHERE s.bid.bid = :bid",

                    BigDecimal.class

                )
                .setParameter(
                        "bid",
                        branch.getBid())
                .getSingleResult();

            if(salesAmount == null) {

                salesAmount =
                        BigDecimal.ZERO;
            }

            int companyId =
                branch.getFid()
                      .getCid()
                      .getCid();

            CommissionRules rule =
                commissionRuleService
                .getMatchingRule(
                        companyId,
                        salesAmount);

            BigDecimal percent =
                    BigDecimal.ZERO;

            BigDecimal royalty =
                    BigDecimal.ZERO;

           if(rule != null &&
            salesAmount.compareTo(BigDecimal.ZERO) > 0) {

             percent = rule.getCoPercentage();

             royalty =
                 salesAmount.multiply(percent)
                 .divide(new BigDecimal("100"));
         }

            rows.add(

                new RoyaltyRow(

                    branch.getBranchName(),
                    salesAmount,
                    percent,
                    royalty
                )
            );
        }

        return rows;
    }

    @Override
    public BigDecimal getTotalRoyalty(
            int franchiseId) {

        return getRoyaltyReport(franchiseId)
                .stream()
                .map(RoyaltyRow::getRoyaltyAmount)
                .reduce(
                    BigDecimal.ZERO,
                    BigDecimal::add
                );
    }

    @Override
    public BigDecimal getTotalSales(
            int franchiseId) {

        BigDecimal sales =
            em.createQuery(

                "SELECT SUM(s.totalAmount) " +
                "FROM Sales s " +
                "WHERE s.bid.fid.fid = :fid",

                BigDecimal.class

            )
            .setParameter("fid",
                    franchiseId)
            .getSingleResult();

        return sales == null
                ? BigDecimal.ZERO
                : sales;
    }
}