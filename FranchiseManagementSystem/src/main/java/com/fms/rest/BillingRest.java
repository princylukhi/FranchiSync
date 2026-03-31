package com.fms.rest;

import com.fms.entity.Sales;
import com.fms.entity.Branches;
import com.fms.entity.Users;
import com.fms.service.BillingServiceLocal;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Stateless
@Path("/billing")
public class BillingRest {

    @EJB
    private BillingServiceLocal billingService;

    @PersistenceContext(unitName = "FranchisePU")
    private EntityManager em;

    // 🔥 1. Create Sale
    @POST
    @Path("/sale")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSale(Sales sale) {

        try {
            // 🔥 IMPORTANT: attach managed entities
            int bid = sale.getBid().getBid();
            int staffId = sale.getStaffId().getUid();

            Branches branch = em.find(Branches.class, bid);
            Users staff = em.find(Users.class, staffId);

            if (branch == null || staff == null) {
                return Response.status(400).entity("Invalid Branch or Staff").build();
            }

            sale.setBid(branch);
            sale.setStaffId(staff);

            Sales created = billingService.createSale(sale);

            return Response.ok(created.getSid()).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity(e.toString()).build();
        }
    }

    // 🔥 2. Add Item to Sale
    @POST
    @Path("/add-item/{saleId}/{productId}/{qty}")
    public Response addItem(@PathParam("saleId") int saleId,
                           @PathParam("productId") int productId,
                           @PathParam("qty") int qty) {

        try {
            billingService.addSaleItem(saleId, productId, qty);
            return Response.ok("Item Added").build();

        } catch (Exception e) {
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

    // 🔥 3. Complete Sale (Generate Invoice)
    @POST
    @Path("/complete/{saleId}")
    public Response completeSale(@PathParam("saleId") int saleId) {

        billingService.completeSale(saleId);

        return Response.ok("Sale Completed + Invoice Generated").build();
    }
}