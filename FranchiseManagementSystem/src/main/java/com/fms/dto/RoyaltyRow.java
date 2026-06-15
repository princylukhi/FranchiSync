package com.fms.dto;

import java.math.BigDecimal;

public class RoyaltyRow {

    private String branchName;
    private BigDecimal salesAmount;
    private BigDecimal commissionPercent;
    private BigDecimal royaltyAmount;

    public RoyaltyRow(
            String branchName,
            BigDecimal salesAmount,
            BigDecimal commissionPercent,
            BigDecimal royaltyAmount) {

        this.branchName = branchName;
        this.salesAmount = salesAmount;
        this.commissionPercent = commissionPercent;
        this.royaltyAmount = royaltyAmount;
    }

    public String getBranchName() {
        return branchName;
    }

    public BigDecimal getSalesAmount() {
        return salesAmount;
    }

    public BigDecimal getCommissionPercent() {
        return commissionPercent;
    }

    public BigDecimal getRoyaltyAmount() {
        return royaltyAmount;
    }
}