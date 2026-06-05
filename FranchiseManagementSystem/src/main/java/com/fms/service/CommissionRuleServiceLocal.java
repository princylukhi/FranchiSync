package com.fms.service;

import com.fms.entity.CommissionRules;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface CommissionRuleServiceLocal {

    String addRule(CommissionRules rule);

    String updateRule(CommissionRules rule);

    void deactivateRule(int coid);

    void activateRule(int coid);

    List<CommissionRules> getRulesByCompany(int companyId);
    
    CommissionRules getMatchingRule(
        int companyId,
        java.math.BigDecimal salesAmount);

}