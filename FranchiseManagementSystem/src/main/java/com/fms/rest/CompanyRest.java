package com.fms.rest;

import com.fms.entity.CompanyRegistrationRequests;
import com.fms.entity.Companies;
import com.fms.service.CompanyServiceLocal;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.nio.file.Files;

import java.util.*;

@Path("/company")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompanyRest {

    @EJB
    private CompanyServiceLocal companyService;
    
    // 5️⃣ Get Company Logo

            @GET
            @Path("/logo/{fileName}")
            @Produces({"image/png", "image/jpg", "image/jpeg"})
            public Response getLogo(
                    @PathParam("fileName") String fileName) {

                try {

                    String path =
                            System.getProperty("com.sun.aas.instanceRoot")
                            + File.separator
                            + "company-logos"
                            + File.separator
                            + fileName;

                    File file = new File(path);

                    if (!file.exists()) {

                        return Response.status(404).build();
                    }

                    return Response.ok(file)
                            .type(Files.probeContentType(file.toPath()))
                            .build();

                } catch (Exception e) {

                    e.printStackTrace();

                    return Response.status(500).build();
                }
            }

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