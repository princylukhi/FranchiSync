package com.fms.rest;

import com.fms.entity.CompanyRegistrationRequests;
import com.fms.entity.Companies;
import com.fms.service.CompanyServiceLocal;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.*;

@Path("/company")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompanyRest {

    @EJB
    private CompanyServiceLocal companyService;

    // 1️⃣ Submit Request
    @POST
    @Path("/request")
    public String requestCompany(CompanyRegistrationRequests req) {

        companyService.submitCompanyRequest(req);

        return "Request submitted";
    }

    // 2️⃣ Get Pending Requests
    @GET
    @Path("/pending")
    public List<Map<String, Object>> getPending() {

        List<CompanyRegistrationRequests> list = companyService.getPendingRequests();

        List<Map<String, Object>> result = new ArrayList<>();

        for (CompanyRegistrationRequests r : list) {

            Map<String, Object> map = new HashMap<>();

            map.put("id", r.getCrid());
            map.put("companyName", r.getCompanyName());
            map.put("email", r.getEmail());
            map.put("status", r.getStatus());

            result.add(map);
        }

        return result;
    }

    // 3️⃣ Approve Company (IMPORTANT FIX)
    @PUT
    @Path("/approve/{id}")
    public String approve(@PathParam("id") int id) {

        companyService.approveCompany(id);

        return "Company Approved + Admin Created";
    }

    // 4️⃣ Reject Company
    @PUT
    @Path("/reject/{id}")
    public String reject(@PathParam("id") int id) {

        companyService.rejectCompany(id);

        return "Company Rejected";
    }
}