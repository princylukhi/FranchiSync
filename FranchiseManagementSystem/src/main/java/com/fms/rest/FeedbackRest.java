package com.fms.rest;

import com.fms.entity.Feedbacks;
import com.fms.entity.Users;
import com.fms.service.FeedbackServiceLocal;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Stateless
@Path("/feedback")
public class FeedbackRest {

    @EJB
    private FeedbackServiceLocal feedbackService;

    // 🔥 1. Submit Feedback
    @POST
    @Path("/submit/{userId}/{companyId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response submit(@PathParam("userId") int userId,
                           @PathParam("companyId") int companyId,
                           Feedbacks feedback) {

        try {

            // 🔥 IMPORTANT FIX (avoid null crash)
            if (feedback == null) {
                return Response.status(400).entity("Invalid JSON").build();
            }

            // 🔥 Only set required fields HERE (not in JSON)
            feedback.setFeedbackDate(new java.util.Date());

            feedbackService.submitFeedback(feedback, userId, companyId);

            return Response.ok("Feedback Submitted").build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    // 🔥 2. Get Feedback by Company
    @GET
    @Path("/company/{cid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByCompany(@PathParam("cid") int cid) {

        List<Feedbacks> list = feedbackService.getFeedbacksByCompany(cid);

        List<Object> result = list.stream().map(f -> new Object() {
            public int id = f.getFid();
            public String message = f.getMessage();
            public int rating = f.getRating();
            public Object date = f.getFeedbackDate();
            public String user = f.getUid().getName();
        }).collect(java.util.stream.Collectors.toList());

        return Response.ok(result).build();
    }

    // 🔥 3. Get All Feedback (Admin)
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {

        List<Feedbacks> list = feedbackService.getAllFeedbacks();

        List<Object> result = list.stream().map(f -> new Object() {
            public int id = f.getFid();
            public String message = f.getMessage();
            public int rating = f.getRating();
        }).collect(java.util.stream.Collectors.toList());

        return Response.ok(result).build();
    }
}