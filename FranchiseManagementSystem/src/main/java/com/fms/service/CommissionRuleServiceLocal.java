package com.fms.service;

import com.fms.entity.CommissionRules;
import jakarta.ejb.Local;
import java.util.List;

@Local
public interface CommissionRuleServiceLocal {

    void addRule(CommissionRules rule);

    void updateRule(CommissionRules rule);

    void deactivateRule(int coid);

    void activateRule(int coid);

    List<CommissionRules> getRulesByCompany(int companyId);

}