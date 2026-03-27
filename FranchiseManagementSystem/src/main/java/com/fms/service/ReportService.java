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

        Query q = em.createQuery(
            "SELECT SUM(s.totalAmount) FROM Sales s WHERE DATE(s.saleDate) = CURRENT_DATE");

        return q.getSingleResult() != null ? 
            ((Number) q.getSingleResult()).doubleValue() : 0;
    }

    // Monthly Sales
    @Override
    public double getMonthlySales() {

        Query q = em.createQuery(
            "SELECT SUM(s.totalAmount) FROM Sales s WHERE MONTH(s.saleDate) = MONTH(CURRENT_DATE)");

        return q.getSingleResult() != null ? 
            ((Number) q.getSingleResult()).doubleValue() : 0;
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