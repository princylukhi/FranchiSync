package com.fms.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.Date;
import java.util.List;

import com.fms.entity.Invoices;

@Stateless
public class InvoiceService implements InvoiceServiceLocal {

    @PersistenceContext(unitName = "FranchisePU")
    private EntityManager em;

    // Create invoice
    @Override
    public void createInvoice(Invoices invoice) {

        invoice.setInvoiceDate(new Date());

        String invoiceNumber =
                "INV-" + System.currentTimeMillis();

        invoice.setInvoiceNumber(invoiceNumber);

        // Take amount from sale
        invoice.setInvoiceAmount(
                invoice.getSid().getTotalAmount()
        );

        invoice.setStatus("GENERATED"); 

        em.persist(invoice);
    }

    // Get all invoices
    @Override
    public List<Invoices> getAllInvoices() {

        Query q = em.createNamedQuery("Invoices.findAll");

        return q.getResultList();
    }

    // Get invoice by ID
    @Override
    public Invoices getInvoiceById(int id) {

        return em.find(Invoices.class, id);
    }
    
    @Override
    public List<Invoices> getInvoicesByBranch(int bid) {

        return em.createQuery(
            "SELECT i FROM Invoices i " +
            "WHERE i.sid.bid.bid = :bid " +
            "ORDER BY i.invoiceDate DESC",
            Invoices.class
        )
        .setParameter("bid", bid)
        .getResultList();
    }
    
    
}