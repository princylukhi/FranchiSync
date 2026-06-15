package com.fms.bean;

import com.fms.entity.Invoices;
import com.fms.service.BillingServiceLocal;
import com.fms.service.CustomerServiceLocal;
import com.fms.service.InvoiceServiceLocal;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Named
@ViewScoped
public class StaffDashboardBean implements Serializable {

    @EJB
    private BillingServiceLocal billingService;

    @EJB
    private InvoiceServiceLocal invoiceService;

    @EJB
    private CustomerServiceLocal customerService;

    @Inject
    private LoginBean loginBean;

    private Long todayBills;
    private Long totalInvoices;
    private Long totalCustomers;
    private BigDecimal todayRevenue;
    private List<Invoices> recentInvoices;
    
    private String companyName;
    private String branchName;

    @PostConstruct
    public void init() {

        int branchId =
                loginBean.getLoggedUser()
                         .getBid()
                         .getBid();
        
        companyName =
                loginBean.getLoggedUser()
                         .getBid()
                         .getFid()
                         .getCid()
                         .getCompanyName();

        branchName =
                loginBean.getLoggedUser()
                         .getBid()
                         .getBranchName();

        todayBills =
                billingService.getTodayBills(branchId);

        totalInvoices =
                invoiceService.getTotalInvoices(branchId);

        totalCustomers =
                customerService.getTotalCustomers(branchId);

        todayRevenue =
                billingService.getTodayRevenue(branchId);
        
        recentInvoices =
                invoiceService.getRecentInvoices(branchId);
    }
    
    public String getFormattedRevenue() {

    double amount = todayRevenue.doubleValue();

    if (amount >= 10000000) {
        return String.format("%.1fCr", amount / 10000000);
    }

    if (amount >= 100000) {
        return String.format("%.1fL", amount / 100000);
    }

    if (amount >= 1000) {
        return String.format("%.1fK", amount / 1000);
    }

    return String.format("%.0f", amount);
    }

    public Long getTodayBills() {
        return todayBills;
    }

    public Long getTotalInvoices() {
        return totalInvoices;
    }

    public Long getTotalCustomers() {
        return totalCustomers;
    }

    public BigDecimal getTodayRevenue() {
        return todayRevenue;
    }
    
    public List<Invoices> getRecentInvoices() {
        return recentInvoices;
    }
    
    public String getCompanyName() {
        return companyName;
    }

    public String getBranchName() {
        return branchName;
    }
    
}