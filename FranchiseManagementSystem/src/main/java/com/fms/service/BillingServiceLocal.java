package com.fms.service;

import jakarta.ejb.Local;
import java.util.List;
import com.fms.entity.Sales;
import com.fms.entity.SaleItems;

@Local
public interface BillingServiceLocal {

    public Sales createSale(Sales sale);

    public void addSaleItem(int saleId, int productId, int quantity);

    public void completeSale(int saleId);

}