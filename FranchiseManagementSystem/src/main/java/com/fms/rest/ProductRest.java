package com.fms.rest;

import com.fms.entity.Products;
import com.fms.entity.Companies;
import com.fms.service.ProductServiceLocal;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.List;

@Stateless
@Path("/products")
public class ProductRest {

    @EJB
    private ProductServiceLocal productService;

    @PersistenceContext(unitName = "FranchisePU")
    private EntityManager em;

    // 🔥 1. Add Product
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addProduct(Products product) {

        try {
            int cid = product.getCid().getCid();

            Companies company = em.find(Companies.class, cid);

            if (company == null) {
                return Response.status(400).entity("Invalid Company ID").build();
            }

            product.setCid(company);

            productService.addProduct(product);

            return Response.ok("Product Added").build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity(e.toString()).build();
        }
    }

    // 🔥 2. Get All Products by Company (SAFE JSON)
    @GET
    @Path("/company/{cid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts(@PathParam("cid") int cid) {

        List<Products> list = productService.getProductsByCompany(cid);

        List<Object> result = list.stream().map(p -> {
            return new Object() {
                public Integer pid = p.getPid();
                public String productName = p.getProductName();
                public BigDecimal price = p.getPrice();
                public boolean isActive = p.getIsActive();
                public Integer companyId = p.getCid().getCid();
                public String companyName = p.getCid().getCompanyName();
            };
        }).collect(java.util.stream.Collectors.toList());

        return Response.ok(result).build();
    }

    // 🔥 3. Get Active Products (for billing)
    @GET
    @Path("/active/{cid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActive(@PathParam("cid") int cid) {

        List<Products> list = productService.getActiveProducts(cid);

        List<Object> result = list.stream().map(p -> {
            return new Object() {
                public Integer pid = p.getPid();
                public String productName = p.getProductName();
                public BigDecimal price = p.getPrice();
            };
        }).collect(java.util.stream.Collectors.toList());

        return Response.ok(result).build();
    }

    // 🔥 4. Update Product
    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProduct(@PathParam("id") int id, Products p) {

        Products existing = em.find(Products.class, id);

        if (existing == null) {
            return Response.status(404).entity("Product not found").build();
        }

        existing.setProductName(p.getProductName());
        existing.setPrice(p.getPrice());

        productService.updateProduct(existing);

        return Response.ok("Product Updated").build();
    }

    // 🔥 5. Update Price Only
    @PUT
    @Path("/price/{id}/{price}")
    public Response updatePrice(@PathParam("id") int id,
                               @PathParam("price") double price) {

        productService.updateProductPrice(id, BigDecimal.valueOf(price));

        return Response.ok("Price Updated").build();
    }

    // 🔥 6. Activate Product
    @PUT
    @Path("/activate/{id}")
    public Response activate(@PathParam("id") int id) {

        productService.activateProduct(id);

        return Response.ok("Product Activated").build();
    }

    // 🔥 7. Deactivate Product
    @PUT
    @Path("/deactivate/{id}")
    public Response deactivate(@PathParam("id") int id) {

        productService.deactivateProduct(id);

        return Response.ok("Product Deactivated").build();
    }
}