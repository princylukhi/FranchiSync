package com.fms.bean;

import com.fms.entity.Invoices;
import com.fms.service.InvoiceServiceLocal;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class InvoiceBean implements Serializable {

    @EJB
    private InvoiceServiceLocal invoiceService;

    @Inject
    private LoginBean loginBean;

    private List<Invoices> invoices;

    @PostConstruct
    public void init() {

        int branchId =
                loginBean.getLoggedUser()
                        .getBid()
                        .getBid();

        invoices =
                invoiceService.getInvoicesByBranch(branchId);
    }

    public List<Invoices> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoices> invoices) {
        this.invoices = invoices;
    }
}