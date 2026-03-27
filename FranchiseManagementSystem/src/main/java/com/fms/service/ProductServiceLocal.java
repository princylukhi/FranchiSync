package com.fms.service;

import jakarta.ejb.Local;
import java.util.List;
import java.math.BigDecimal;
import com.fms.entity.Products;

@Local
public interface ProductServiceLocal {

    // Add new product
    public void addProduct(Products product);

    // Update product details (name + price)
    public void updateProduct(Products product);

    // Update only product price
    public void updateProductPrice(int productId, BigDecimal newPrice);

    // Get all products of company
    public List<Products> getProductsByCompany(int cid);

    // Get only active products (for billing)
    public List<Products> getActiveProducts(int cid);

    // Activate product
    public void activateProduct(int productId);

    // Deactivate product
    public void deactivateProduct(int productId);

}