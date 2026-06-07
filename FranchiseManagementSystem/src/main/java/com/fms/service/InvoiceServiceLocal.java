package com.fms.service;

import jakarta.ejb.Local;
import java.util.List;
import com.fms.entity.Invoices;

@Local
public interface InvoiceServiceLocal {

    public void createInvoice(Invoices invoice);

    public List<Invoices> getAllInvoices();

    public Invoices getInvoiceById(int id);
    
    List<Invoices> getInvoicesByBranch(int bid);
    
    List<Object[]> getMonthlyInvoiceTrendByStaff(
        int staffId
    );
    
    Long getTotalInvoices(int bid);
    
    List<Invoices> getRecentInvoices(int bid);

}