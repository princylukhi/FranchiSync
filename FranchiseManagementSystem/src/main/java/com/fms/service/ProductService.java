package com.fms.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;
import java.math.BigDecimal;

import com.fms.entity.Products;

@Stateless
public class ProductService implements ProductServiceLocal {

    @PersistenceContext(unitName = "FranchisePU")
    private EntityManager em;

    // 1️⃣ Add Product
    @Override
    public void addProduct(Products product) {

        product.setIsActive(true); // default active

        em.persist(product);
    }

    // 2️⃣ Update Product (Name + Price)
    @Override
    public void updateProduct(Products product) {

        Products existing = em.find(Products.class, product.getPid());

        if (existing != null) {
            existing.setProductName(product.getProductName());
            existing.setPrice(product.getPrice());

            em.merge(existing);
        }
    }

    // 3️⃣ Update Only Price
    @Override
    public void updateProductPrice(int productId, BigDecimal newPrice) {

        Products product = em.find(Products.class, productId);

        if (product != null) {
            product.setPrice(newPrice);

            em.merge(product);
        }
    }

    // 4️⃣ Get All Products of Company
    @Override
    public List<Products> getProductsByCompany(int cid) {

        Query q = em.createQuery(
            "SELECT p FROM Products p WHERE p.cid.cid = :cid");

        q.setParameter("cid", cid);

        return q.getResultList();
    }

    // 5️⃣ Get Active Products (Catalog / Billing)
    @Override
    public List<Products> getActiveProducts(int cid) {

        Query q = em.createQuery(
            "SELECT p FROM Products p WHERE p.cid.cid = :cid AND p.isActive = true");

        q.setParameter("cid", cid);

        return q.getResultList();
    }

    // 6️⃣ Activate Product
    @Override
    public void activateProduct(int productId) {

        Products product = em.find(Products.class, productId);

        if (product != null) {
            product.setIsActive(true);

            em.merge(product);
        }
    }

    // 7️⃣ Deactivate Product
    @Override
    public void deactivateProduct(int productId) {

        Products product = em.find(Products.class, productId);

        if (product != null) {
            product.setIsActive(false);

            em.merge(product);
        }
    }
}