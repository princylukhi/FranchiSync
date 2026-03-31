package com.fms.rest;

import com.fms.service.ReportServiceLocal;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Stateless
@Path("/report")
public class ReportRest {

    @EJB
    private ReportServiceLocal reportService;

    // 🔥 1. Total Sales
    @GET
    @Path("/total")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTotalSales() {

        double total = reportService.getTotalSales();

        return Response.ok(
            new Object() {
                public double totalSales = total;
            }
        ).build();
    }

    // 🔥 2. Daily Sales
    @GET
    @Path("/daily")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDailySales() {

        double total = reportService.getDailySales();

        return Response.ok(
            new Object() {
                public double dailySales = total;
            }
        ).build();
    }

    // 🔥 3. Monthly Sales
    @GET
    @Path("/monthly")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMonthlySales() {

        double total = reportService.getMonthlySales();

        return Response.ok(
            new Object() {
                public double monthlySales = total;
            }
        ).build();
    }

    // 🔥 4. Branch-wise Sales (Chart Data)
    @GET
    @Path("/branch-wise")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBranchWise() {

        List<Object[]> list = reportService.getBranchWiseSales();

        List<Object> result = list.stream().map(r -> {
            return new Object() {
                public String branchName = (String) r[0];
                public double totalSales = ((Number) r[1]).doubleValue();
            };
        }).collect(java.util.stream.Collectors.toList());

        return Response.ok(result).build();
    }

    // 🔥 5. Product-wise Sales (Chart Data)
    @GET
    @Path("/product-wise")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductWise() {

        List<Object[]> list = reportService.getProductWiseSales();

        List<Object> result = list.stream().map(r -> {
            return new Object() {
                public String productName = (String) r[0];
                public int quantitySold = ((Number) r[1]).intValue();
            };
        }).collect(java.util.stream.Collectors.toList());

        return Response.ok(result).build();
    }
}