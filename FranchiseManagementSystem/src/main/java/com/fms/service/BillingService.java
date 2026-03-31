package com.fms.service;

import jakarta.ejb.Stateless;
import jakarta.ejb.EJB;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Date;

import com.fms.entity.Sales;
import com.fms.entity.SaleItems;
import com.fms.entity.Products;
import com.fms.entity.Inventory;
import com.fms.entity.Invoices;
import java.util.List;

@Stateless
public class BillingService implements BillingServiceLocal {

    @PersistenceContext(unitName = "FranchisePU")
    private EntityManager em;

    @EJB
    private InventoryServiceLocal inventoryService;

    @EJB
    private InvoiceServiceLocal invoiceService;

    // 1️⃣ Create Sale
    @Override
    public Sales createSale(Sales sale) {

        sale.setSaleDate(new Date());
        sale.setTotalAmount(java.math.BigDecimal.ZERO);

        em.persist(sale);

        return sale;
    }

//    // 2️⃣ Add Product to Bill
//    @Override
//    public void addSaleItem(int saleId, int productId, int quantity) {
//
//        Sales sale = em.find(Sales.class, saleId);
//        Products product = em.find(Products.class, productId);
//
//        // Create Sale Item
//        SaleItems item = new SaleItems();
//
//        item.setSid(sale);
//        item.setPid(product);
//        item.setQuantity(quantity);
//        item.setPrice(product.getPrice());
//
//        em.persist(item);
//
//        // Update Total
//        double total = sale.getTotalAmount().doubleValue();
//        double itemTotal = product.getPrice().doubleValue() * quantity;
//
//        sale.setTotalAmount(
//            new java.math.BigDecimal(total + itemTotal)
//        );
//
//        em.merge(sale);
//
//        // Deduct Inventory
//        Inventory inv = (Inventory) em.createQuery(
//            "SELECT i FROM Inventory i WHERE i.pid.pid = :pid AND i.bid.bid = :bid")
//            .setParameter("pid", productId)
//            .setParameter("bid", sale.getBid().getBid())
//            .getSingleResult();
//
//        inventoryService.decreaseStock(inv.getInid(), quantity);
//    }

    
    @Override
        public void addSaleItem(int saleId, int productId, int quantity) {

            Sales sale = em.find(Sales.class, saleId);
            Products product = em.find(Products.class, productId);

            if (sale == null || product == null) {
                throw new RuntimeException("Invalid Sale or Product");
            }

            // 🔥 Create Sale Item
            SaleItems item = new SaleItems();
            item.setSid(sale);
            item.setPid(product);
            item.setQuantity(quantity);
            item.setPrice(product.getPrice());

            em.persist(item);

            // 🔥 Update Total
            double total = sale.getTotalAmount().doubleValue();
            double itemTotal = product.getPrice().doubleValue() * quantity;

            sale.setTotalAmount(
                new java.math.BigDecimal(total + itemTotal)
            );

            em.merge(sale);

            // 🔥 FIXED INVENTORY QUERY (IMPORTANT)
            List<Inventory> list = em.createQuery(
                "SELECT i FROM Inventory i WHERE i.pid.pid = :pid AND i.bid.bid = :bid",
                Inventory.class)
                .setParameter("pid", productId)
                .setParameter("bid", sale.getBid().getBid())
                .getResultList();

            if (list.isEmpty()) {
                throw new RuntimeException("Inventory not found for this product in branch");
            }

            Inventory inv = list.get(0); // take first match

            // 🔥 Deduct stock
            if (inv.getQuantity() < quantity) {
                throw new RuntimeException("Insufficient stock");
            }

            inventoryService.decreaseStock(inv.getInid(), quantity);
        }
            // 3️⃣ Complete Sale (Generate Invoice)
    @Override
    public void completeSale(int saleId) {

        Sales sale = em.find(Sales.class, saleId);

        Invoices invoice = new Invoices();

        invoice.setSid(sale);

        invoiceService.createInvoice(invoice);
    }
}