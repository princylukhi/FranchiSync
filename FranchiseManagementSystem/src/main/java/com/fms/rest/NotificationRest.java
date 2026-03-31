package com.fms.rest;

import com.fms.entity.Notifications;
import com.fms.service.NotificationServiceLocal;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Stateless
@Path("/notifications")
public class NotificationRest {

    @EJB
    private NotificationServiceLocal notificationService;

    // 🔥 1. Send Notification
    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response send(Notifications n) {

        notificationService.sendNotification(
                n.getRecipientEmail(),
                n.getSubject(),
                n.getMessage(),
                n.getNotificationType()
        );

        return Response.ok("Notification Sent").build();
    }

    // 🔥 2. Get All Notifications
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {

        List<Notifications> list = notificationService.getAllNotifications();

        // ✅ Avoid recursion / clean response
        List<Object> result = list.stream().map(n -> new Object() {
            public int id = n.getNid();
            public String email = n.getRecipientEmail();
            public String subject = n.getSubject();
            public String message = n.getMessage();
            public String type = n.getNotificationType();
            public Object date = n.getSentDate();
        }).collect(java.util.stream.Collectors.toList());

        return Response.ok(result).build();
    }
}