package com.fms.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;

import com.fms.entity.Inventory;

@Stateless
public class InventoryService implements InventoryServiceLocal {

    @PersistenceContext(unitName = "FranchisePU")
    private EntityManager em;

    // Add new stock
    @Override
    public void addStock(Inventory inventory) {

        em.persist(inventory);
    }

    // Update full inventory
    @Override
    public void updateStock(Inventory inventory) {

        em.merge(inventory);
    }

    // Get inventory by branch
    @Override
    public List<Inventory> getInventoryByBranch(int bid) {

        Query q = em.createQuery(
            "SELECT i FROM Inventory i WHERE i.bid.bid = :bid");

        q.setParameter("bid", bid);

        return q.getResultList();
    }

    // Increase stock
    @Override
    public void increaseStock(int inventoryId, int quantity) {

        Inventory inv = em.find(Inventory.class, inventoryId);

        inv.setQuantity(inv.getQuantity() + quantity);

        em.merge(inv);
    }

    // Decrease stock (VERY IMPORTANT FOR BILLING)
    @Override
    public void decreaseStock(int inventoryId, int quantity) {

        Inventory inv = em.find(Inventory.class, inventoryId);

        int newQty = inv.getQuantity() - quantity;

        if(newQty >= 0){
            inv.setQuantity(newQty);
            em.merge(inv);
        } else {
            throw new RuntimeException("Insufficient stock");
        }
    }

    // Low stock alert
    @Override
    public List<Inventory> getLowStock(int bid) {

        Query q = em.createQuery(
            "SELECT i FROM Inventory i WHERE i.bid.bid = :bid AND i.quantity <= i.minThreshold");

        q.setParameter("bid", bid);

        return q.getResultList();
    }
}