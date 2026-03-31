package com.fms.rest;

import com.fms.entity.Companies;
import com.fms.entity.FranchiseRequests;
import com.fms.entity.Franchises;
import com.fms.entity.Users;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Date;
import java.util.List;

@Stateless
@Path("/franchise")
public class FranchiseRest {

    @PersistenceContext(unitName = "FranchisePU")
    private EntityManager em;

    // 🔥 1. Submit Franchise Request
    @POST
    @Path("/request")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRequest(FranchiseRequests req) {
        try {
            req.setStatus("PENDING");
            req.setRequestDate(new Date());

            em.persist(req);

            return Response.ok("Franchise Request Submitted").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error: " + e.getMessage())
                    .build();
        }
    }

    // 🔥 2. Get All Requests
    @GET
@Path("/requests")
@Produces(MediaType.APPLICATION_JSON)
public Response getAllRequests() {

    List<FranchiseRequests> list = em.createNamedQuery(
            "FranchiseRequests.findAll",
            FranchiseRequests.class
    ).getResultList();

    List<Object> result = list.stream().map(r -> {
        return new Object() {
            public Integer frid = r.getFrid();
            public String ownerName = r.getOwnerName();
            public String email = r.getEmail();
            public String phone = r.getPhone();
            public String status = r.getStatus();
            public Date requestDate = r.getRequestDate();
            public Integer cid = r.getCid().getCid();
            public String companyName = r.getCid().getCompanyName();
        };
    }).collect(java.util.stream.Collectors.toList());

    return Response.ok(result).build();
}

    // 🔥 3. Approve Request → Create Franchise
    @PUT
    @Path("/approve/{reqId}/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response approveRequest(@PathParam("reqId") int reqId,
                                  @PathParam("userId") int userId) {

        FranchiseRequests req = em.find(FranchiseRequests.class, reqId);
        Users user = em.find(Users.class, userId);

        if (req == null || user == null) {
            return Response.status(404).entity("Request or User not found").build();
        }

        // ✅ Update request
        req.setStatus("APPROVED");
        req.setApprovedDate(new Date());
        em.merge(req);

        // ✅ Create Franchise
        Franchises f = new Franchises();
        f.setStatus("ACTIVE");
        f.setCreatedDate(new Date());

        Companies company = req.getCid();
        f.setCid(company);
        f.setOwnerUserId(user);

        em.persist(f);

        return Response.ok("Franchise Approved & Created").build();
    }

    // 🔥 4. Reject Request
    @PUT
    @Path("/reject/{id}")
    public Response rejectRequest(@PathParam("id") int id) {

        FranchiseRequests req = em.find(FranchiseRequests.class, id);

        if (req == null) {
            return Response.status(404).entity("Request not found").build();
        }

        req.setStatus("REJECTED");
        em.merge(req);

        return Response.ok("Request Rejected").build();
    }

    // 🔥 5. Get All Franchises
    @GET
        @Path("/all")
        @Produces(MediaType.APPLICATION_JSON)
        public Response getAllFranchises() {

            List<Franchises> list = em.createNamedQuery(
                    "Franchises.findAll",
                    Franchises.class
            ).getResultList();

            List<Object> result = list.stream().map(f -> {
                return new Object() {
                    public Integer fid = f.getFid();
                    public String status = f.getStatus();
                    public Date createdDate = f.getCreatedDate();
                    public String companyName = f.getCid().getCompanyName();
                    public String ownerName = f.getOwnerUserId().getName();
                };
            }).collect(java.util.stream.Collectors.toList());

            return Response.ok(result).build();
        }

    // 🔥 6. Get Franchise by Company
    @GET
        @Path("/company/{cid}")
        @Produces(MediaType.APPLICATION_JSON)
        public Response getByCompany(@PathParam("cid") int cid) {

            List<Franchises> list = em.createQuery(
                    "SELECT f FROM Franchises f WHERE f.cid.cid = :cid",
                    Franchises.class
            ).setParameter("cid", cid).getResultList();

            List<Object> result = list.stream().map(f -> {
                return new Object() {
                    public Integer fid = f.getFid();
                    public String status = f.getStatus();
                    public Date createdDate = f.getCreatedDate();
                    public String ownerName = f.getOwnerUserId().getName();
                };
            }).collect(java.util.stream.Collectors.toList());

            return Response.ok(result).build();
        }
}