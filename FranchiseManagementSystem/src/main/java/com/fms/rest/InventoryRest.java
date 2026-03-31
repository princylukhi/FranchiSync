package com.fms.rest;

import com.fms.entity.Inventory;
import com.fms.entity.Branches;
import com.fms.entity.Products;
import com.fms.service.InventoryServiceLocal;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Stateless
@Path("/inventory")
public class InventoryRest {

    @EJB
    private InventoryServiceLocal inventoryService;

    @PersistenceContext(unitName = "FranchisePU")
    private EntityManager em;

    // 🔥 1. Add Stock
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addStock(Inventory inv) {

        try {
            // 🔥 IMPORTANT: attach managed entities
            int bid = inv.getBid().getBid();
            int pid = inv.getPid().getPid();

            Branches branch = em.find(Branches.class, bid);
            Products product = em.find(Products.class, pid);

            if (branch == null || product == null) {
                return Response.status(400).entity("Invalid Branch or Product").build();
            }

            inv.setBid(branch);
            inv.setPid(product);

            inventoryService.addStock(inv);

            return Response.ok("Stock Added").build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity(e.toString()).build();
        }
    }

    // 🔥 2. Get Inventory by Branch
    @GET
    @Path("/branch/{bid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByBranch(@PathParam("bid") int bid) {

        List<Inventory> list = inventoryService.getInventoryByBranch(bid);

        List<Object> result = list.stream().map(i -> {

            return new Object() {
                public Integer inid = i.getInid();
                public int quantity = i.getQuantity();
                public int minThreshold = i.getMinThreshold();

                public Integer branchId = i.getBid().getBid();
                public String branchName = i.getBid().getBranchName();

                public Integer productId = i.getPid().getPid();
                public String productName = i.getPid().getProductName();
            };

        }).collect(java.util.stream.Collectors.toList());

        return Response.ok(result).build();
    }

    // 🔥 3. Increase Stock
    @PUT
    @Path("/increase/{id}/{qty}")
    public Response increase(@PathParam("id") int id,
                             @PathParam("qty") int qty) {

        inventoryService.increaseStock(id, qty);

        return Response.ok("Stock Increased").build();
    }

    // 🔥 4. Decrease Stock
    @PUT
    @Path("/decrease/{id}/{qty}")
    public Response decrease(@PathParam("id") int id,
                             @PathParam("qty") int qty) {

        try {
            inventoryService.decreaseStock(id, qty);
            return Response.ok("Stock Decreased").build();

        } catch (Exception e) {
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

    // 🔥 5. Low Stock Alert
    @GET
    @Path("/low/{bid}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Inventory> lowStock(@PathParam("bid") int bid) {

        return inventoryService.getLowStock(bid);
    }
}