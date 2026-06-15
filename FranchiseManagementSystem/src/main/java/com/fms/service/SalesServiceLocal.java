package com.fms.service;

import com.fms.entity.Sales;
import jakarta.ejb.Local;
import java.math.BigDecimal;
import java.util.List;

@Local
public interface SalesServiceLocal {

    List<Sales> getRecentSalesByFranchise(int franchiseId);

    Long getTotalOrdersByFranchise(int franchiseId);

    BigDecimal getTotalRevenueByFranchise(int franchiseId);

    List<Object[]> getBranchWiseSales(int franchiseId);
    
    List<Object[]> getSalesDistribution(int franchiseId);

    List<Object[]> getTopRevenueBranches(int franchiseId);
    
    List<Object[]> getMonthlySalesTrend(int branchId);  
    
    List<Object[]> getPaymentModeDistribution(
        int branchId);
    
    List<Object[]> getMonthlySalesRevenueByStaff(int staffId);

    List<Object[]> getPaymentModeDistributionByStaff(int staffId);
    
    List<Object[]> getBranchPerformance(int franchiseId);
    
    BigDecimal getTodaySales(int branchId);
}