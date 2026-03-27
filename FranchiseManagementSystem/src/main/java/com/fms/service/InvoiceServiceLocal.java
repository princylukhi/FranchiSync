package com.fms.service;

import jakarta.ejb.Local;
import java.util.List;
import com.fms.entity.Invoices;

@Local
public interface InvoiceServiceLocal {

    public void createInvoice(Invoices invoice);

    public List<Invoices> getAllInvoices();

    public Invoices getInvoiceById(int id);

}