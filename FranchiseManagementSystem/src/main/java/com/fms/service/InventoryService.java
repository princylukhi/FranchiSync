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

        inventory.setStatus("ACTIVE");
        em.persist(inventory);
        em.flush();
        em.refresh(inventory);
    }   

    // Update full inventory
    @Override
    public void updateStock(Inventory inventory) {

        em.merge(inventory);

        em.flush();

        em.clear();
    }
    
    // Get inventory by branch
    @Override
    public List<Inventory> getInventoryByBranch(int bid) {

        Query q = em.createQuery(
            "SELECT i FROM Inventory i WHERE i.bid.bid = :bid ORDER BY i.lastUpdated DESC");

        q.setParameter("bid", bid);

        return q.getResultList();
    }

    // Increase stock
    @Override
    public void increaseStock(int inventoryId, int quantity) {

        Inventory inv = em.find(Inventory.class, inventoryId);

        inv.setQuantity(inv.getQuantity() + quantity);
        
        inv.setStatus("ACTIVE");

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
            "SELECT i FROM Inventory i WHERE i.bid.bid = :bid AND i.quantity <= i.minThreshold ORDER BY i.lastUpdated DESC");

        q.setParameter("bid", bid);

        return q.getResultList();
    }
    
    @Override
    public Inventory getInventoryByProductAndBranch(
            int productId,
            int branchId) {

        List<Inventory> list =

            em.createQuery(

                "SELECT i FROM Inventory i " +
                "WHERE i.pid.pid = :pid " +
                "AND i.bid.bid = :bid",

                Inventory.class

            )
            .setParameter("pid", productId)
            .setParameter("bid", branchId)
            .getResultList();

        return list.isEmpty()
                ? null
                : list.get(0);
    }
    
    @Override
    public List<Inventory> getAvailableInventory(
            int branchId) {

        return em.createQuery(

            "SELECT i FROM Inventory i " +
            "WHERE i.bid.bid = :bid " +
            "AND i.quantity > 0 " +
            "AND i.status = 'ACTIVE'",

            Inventory.class

        )
        .setParameter("bid", branchId)
        .getResultList();
    }
    
    @Override
public long getTotalProductsByBranch(int branchId) {

    return em.createQuery(

        "SELECT COUNT(i) " +
        "FROM Inventory i " +
        "WHERE i.bid.bid = :bid",

        Long.class

    )
    .setParameter("bid", branchId)
    .getSingleResult();
}

@Override
public long getLowStockCount(int branchId) {

    return em.createQuery(

        "SELECT COUNT(i) " +
        "FROM Inventory i " +
        "WHERE i.bid.bid = :bid " +
        "AND i.quantity <= i.minThreshold",

        Long.class

    )
    .setParameter("bid", branchId)
    .getSingleResult();
}

@Override
public List<Inventory> getRecentInventoryByBranch(int bid) {

    return em.createQuery(

        "SELECT i FROM Inventory i " +
        "WHERE i.bid.bid = :bid " +
        "ORDER BY i.lastUpdated DESC",

        Inventory.class

    )
    .setParameter("bid", bid)
    .setMaxResults(5)
    .getResultList();
}
}