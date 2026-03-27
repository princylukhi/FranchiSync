package com.fms.service;

import jakarta.ejb.Local;
import java.util.List;

@Local
public interface ReportServiceLocal {

    public double getTotalSales();

    public double getDailySales();

    public double getMonthlySales();

    public List<Object[]> getBranchWiseSales();

    public List<Object[]> getProductWiseSales();

}