package com.fms.service;

import com.fms.dto.RoyaltyRow;
import jakarta.ejb.Local;

import java.math.BigDecimal;
import java.util.List;

@Local
public interface RoyaltyServiceLocal {

    List<RoyaltyRow> getRoyaltyReport(int franchiseId);

    BigDecimal getTotalRoyalty(int franchiseId);

    BigDecimal getTotalSales(int franchiseId);
    
    BigDecimal getCurrentMonthSales(int franchiseId);
    
    BigDecimal getCurrentMonthRoyalty(int franchiseId);
}