package com.fms.rest;

import com.fms.entity.Branches;
import com.fms.entity.Franchises;
import com.fms.service.BranchServiceLocal;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Path("/branch")
public class BranchRest {

    @EJB
    private BranchServiceLocal branchService;

    @PersistenceContext(unitName = "FranchisePU")
    private EntityManager em;

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBranch(Branches branch) {

        try {
            // ✅ Validate franchise
            if (branch.getFid() == null || branch.getFid().getFid() == null) {
                return Response.status(400).entity("Franchise ID is required").build();
            }

            int fid = branch.getFid().getFid();

            Franchises franchise = em.find(Franchises.class, fid);

            if (franchise == null) {
                return Response.status(400).entity("Invalid Franchise ID").build();
            }

            // ✅ VERY IMPORTANT → set managed entity
            branch.setFid(franchise);

            // 🔥 SET ALL REQUIRED FIELDS HERE
            if (branch.getStatus() == null) {
                branch.setStatus("ACTIVE");
            }

            // 👉 If your entity has more required fields, set them here similarly

            // ✅ Call service
            branchService.addBranch(branch);

            return Response.status(201).entity("Branch Created Successfully").build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity(e.toString()).build();
        }
    }
    // 🔥 2. GET BRANCHES BY FRANCHISE
    @GET
    @Path("/franchise/{fid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBranchesByFranchise(@PathParam("fid") int fid) {

        List<Branches> list = branchService.getBranchesByFranchise(fid);

        // ✅ Prevent recursion
        List<Object> result = list.stream().map(b -> {
            return new Object() {
                public Integer bid = b.getBid();
                public String name = b.getBranchName();
                public String location = b.getLocation();
                public String status = b.getStatus();
                public Integer franchiseId = b.getFid() != null ? b.getFid().getFid() : null;
            };
        }).collect(Collectors.toList());

        return Response.ok(result).build();
    }

    // 🔥 3. UPDATE BRANCH
    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBranch(@PathParam("id") int id, Branches branch) {

        try {
            Branches existing = em.find(Branches.class, id);

            if (existing == null) {
                return Response.status(404).entity("Branch not found").build();
            }

            // ✅ Update fields
            existing.setBranchName(branch.getBranchName());
            existing.setLocation(branch.getLocation());
            existing.setStatus(branch.getStatus());

            // ❗ DO NOT REMOVE fid
            // Keep existing.getFid()

            em.merge(existing);

            return Response.ok("Branch Updated").build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity(e.toString()).build();
        }
    }

    // 🔥 4. ACTIVATE BRANCH
    @PUT
    @Path("/activate/{id}")
    public Response activateBranch(@PathParam("id") int id) {

        branchService.activateBranch(id);

        return Response.ok("Branch Activated").build();
    }

    // 🔥 5. DEACTIVATE BRANCH
    @PUT
    @Path("/deactivate/{id}")
    public Response deactivateBranch(@PathParam("id") int id) {

        branchService.deactivateBranch(id);

        return Response.ok("Branch Deactivated").build();
    }
}