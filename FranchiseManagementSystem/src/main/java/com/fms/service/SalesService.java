package com.fms.service;

import com.fms.entity.Sales;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.util.List;

@Stateless
public class SalesService implements SalesServiceLocal {

    @PersistenceContext(unitName = "FranchisePU")
    private EntityManager em;

    @Override
public Long getTotalOrdersByFranchise(int franchiseId) {

    return em.createQuery(

        "SELECT COUNT(s) " +
        "FROM Sales s " +
        "WHERE s.bid.fid.fid = :fid",

        Long.class

    )
    .setParameter("fid", franchiseId)
    .getSingleResult();
}

@Override
public BigDecimal getTotalRevenueByFranchise(int franchiseId) {

    BigDecimal revenue =
        em.createQuery(

            "SELECT SUM(s.totalAmount) " +
            "FROM Sales s " +
            "WHERE s.bid.fid.fid = :fid",

            BigDecimal.class

        )
        .setParameter("fid", franchiseId)
        .getSingleResult();

    return revenue == null
            ? BigDecimal.ZERO
            : revenue;
}

@Override
public List<Sales> getRecentSalesByFranchise(int franchiseId) {

    return em.createQuery(

        "SELECT s FROM Sales s " +
        "WHERE s.bid.fid.fid = :fid " +
        "ORDER BY s.saleDate DESC",

        Sales.class

    )
    .setParameter("fid", franchiseId)
    .setMaxResults(10)
    .getResultList();
}

    @Override
    public List<Object[]> getBranchWiseSales(int franchiseId) {

        return em.createQuery(

            "SELECT " +
            "s.bid.branchName, " +
            "COUNT(s), " +
            "SUM(s.totalAmount) " +
            "FROM Sales s " +
            "WHERE s.bid.fid.fid = :fid " +
            "GROUP BY s.bid.branchName",

            Object[].class

        )
        .setParameter("fid", franchiseId)
        .getResultList();
    }

    @Override
    public List<Object[]> getSalesDistribution(
            int franchiseId) {

        return em.createQuery(

            "SELECT s.bid.branchName, " +
            "COALESCE(SUM(s.totalAmount),0) " +
            "FROM Sales s " +
            "WHERE s.bid.fid.fid = :fid " +
            "GROUP BY s.bid.branchName",

            Object[].class

        )
        .setParameter("fid", franchiseId)
        .getResultList();
    }
    
  @Override
public List<Object[]> getTopRevenueBranches(int franchiseId) {

    return em.createQuery(

        "SELECT b.branchName, " +
        "COALESCE(SUM(s.totalAmount),0) " +
        "FROM Branches b " +
        "LEFT JOIN b.salesCollection s " +
        "WHERE b.fid.fid = :fid " +
        "GROUP BY b.branchName " +
        "ORDER BY COALESCE(SUM(s.totalAmount),0) DESC",

        Object[].class

    )
    .setParameter("fid", franchiseId)
    .getResultList();
}

@Override
public List<Object[]> getMonthlySalesTrend(int branchId) {

    return em.createQuery(

        "SELECT FUNCTION('MONTH', s.saleDate), " +
        "COALESCE(SUM(s.totalAmount),0) " +
        "FROM Sales s " +
        "WHERE s.bid.bid = :bid " +
        "GROUP BY FUNCTION('MONTH', s.saleDate) " +
        "ORDER BY FUNCTION('MONTH', s.saleDate)",

        Object[].class

    )
    .setParameter("bid", branchId)
    .getResultList();
}

@Override
public List<Object[]> getPaymentModeDistribution(
        int branchId) {

    return em.createQuery(

        "SELECT s.paymentMode, " +
        "COUNT(s) " +
        "FROM Sales s " +
        "WHERE s.bid.bid = :bid " +
        "GROUP BY s.paymentMode",

        Object[].class

    )
    .setParameter("bid", branchId)
    .getResultList();
}
}