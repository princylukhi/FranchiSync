package com.fms.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;

@Stateless
public class ReportService implements ReportServiceLocal {

    @PersistenceContext(unitName = "FranchisePU")
    private EntityManager em;

    // Total Sales
    @Override
    public double getTotalSales() {

        Query q = em.createQuery("SELECT SUM(s.totalAmount) FROM Sales s");

        return q.getSingleResult() != null ? 
            ((Number) q.getSingleResult()).doubleValue() : 0;
    }

    // Daily Sales
@Override
public double getDailySales() {

    java.util.Calendar cal = java.util.Calendar.getInstance();

    cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
    cal.set(java.util.Calendar.MINUTE, 0);
    cal.set(java.util.Calendar.SECOND, 0);

    java.util.Date start = cal.getTime();

    cal.add(java.util.Calendar.DATE, 1);
    java.util.Date end = cal.getTime();

    Number result = em.createQuery(
        "SELECT SUM(s.totalAmount) FROM Sales s WHERE s.saleDate >= :start AND s.saleDate < :end", Number.class
    )
    .setParameter("start", start)
    .setParameter("end", end)
    .getSingleResult();

    return result != null ? result.doubleValue() : 0;
}

    // Monthly Sales
    @Override
    public double getMonthlySales() {

        java.util.Calendar cal = java.util.Calendar.getInstance();

        // 🔥 start of month
        cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);

        java.util.Date start = cal.getTime();

        // 🔥 next month
        cal.add(java.util.Calendar.MONTH, 1);
        java.util.Date end = cal.getTime();

        Number result = em.createQuery(
            "SELECT SUM(s.totalAmount) FROM Sales s WHERE s.saleDate >= :start AND s.saleDate < :end",
            Number.class   // ✅ VERY IMPORTANT
        )
        .setParameter("start", start)
        .setParameter("end", end)
        .getSingleResult();

        return result != null ? result.doubleValue() : 0;
    }

    // Branch-wise Sales
    @Override
    public List<Object[]> getBranchWiseSales() {

        Query q = em.createQuery(
            "SELECT s.bid.branchName, SUM(s.totalAmount) FROM Sales s GROUP BY s.bid.branchName");

        return q.getResultList();
    }

    // Product-wise Sales
    @Override
    public List<Object[]> getProductWiseSales() {

        Query q = em.createQuery(
            "SELECT si.pid.productName, SUM(si.quantity) FROM SaleItems si GROUP BY si.pid.productName");

        return q.getResultList();
    }
}