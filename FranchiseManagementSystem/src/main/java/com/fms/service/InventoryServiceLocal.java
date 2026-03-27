package com.fms.service;

import jakarta.ejb.Local;
import java.util.List;
import com.fms.entity.Inventory;

@Local
public interface InventoryServiceLocal {

    public void addStock(Inventory inventory);

    public void updateStock(Inventory inventory);

    public List<Inventory> getInventoryByBranch(int bid);

    public void increaseStock(int inventoryId, int quantity);

    public void decreaseStock(int inventoryId, int quantity);

    public List<Inventory> getLowStock(int bid);

}