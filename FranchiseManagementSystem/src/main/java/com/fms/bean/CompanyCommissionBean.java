package com.fms.bean;

import com.fms.entity.CommissionRules;
import com.fms.entity.Companies;
import com.fms.entity.Users;

import com.fms.service.CommissionRuleServiceLocal;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import jakarta.servlet.http.HttpSession;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
public class CompanyCommissionBean
implements Serializable {

    @EJB
    private CommissionRuleServiceLocal
            commissionRuleService;

    private List<CommissionRules> rules =
            new ArrayList<>();

    private CommissionRules rule =
            new CommissionRules();

    private int companyId;

    @PostConstruct
    public void init() {

        try {

            HttpSession session =
                (HttpSession)
                FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSession(false);

            if(session == null) {
                return;
            }

            Users user =
                (Users) session.getAttribute("user");

            if(user == null ||
               user.getCid() == null) {
                return;
            }

            companyId =
                user.getCid().getCid();

            loadRules();

        } catch(Exception e) {

            e.printStackTrace();
        }
        
    }

    public void loadRules() {

        rules =
            commissionRuleService
            .getRulesByCompany(companyId);
    }

    public void addRule() {

        try {

            Companies company =
                    new Companies();

            company.setCid(companyId);

            rule.setCid(company);

            rule.setCreatedDate(
                    new Date()
            );

            rule.setStatus("ACTIVE");

            String result =
    commissionRuleService.addRule(rule);

        if(!result.equals("SUCCESS")) {

            FacesContext.getCurrentInstance()
                .addMessage(
                    null,
                    new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        result,
                        null
                    )
                );

            return;
        }

            loadRules();

            rule =
                new CommissionRules();

        } catch(Exception e) {

            e.printStackTrace();
        }
    }

    public void edit(CommissionRules r) {

        rule = r;
    }

    public void updateRule() {

        String result =
            commissionRuleService
                .updateRule(rule);

        if(!result.equals("SUCCESS")) {

            FacesContext.getCurrentInstance()
                .addMessage(
                    null,
                    new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        result,
                        null
                    )
                );

            return;
        }

        loadRules();

        rule = new CommissionRules();
    }

    public void activate(int coid) {

        commissionRuleService
                .activateRule(coid);

        loadRules();
    }

    public void deactivate(int coid) {

        commissionRuleService
                .deactivateRule(coid);

        loadRules();
    }
    
    public void openAddDialog() {

        rule = new CommissionRules();
    }

    public List<CommissionRules> getRules() {
        return rules;
    }

    public void setRules(List<CommissionRules> rules) {
        this.rules = rules;
    }

    public CommissionRules getRule() {
        return rule;
    }

    public void setRule(CommissionRules rule) {
        this.rule = rule;
    }
}