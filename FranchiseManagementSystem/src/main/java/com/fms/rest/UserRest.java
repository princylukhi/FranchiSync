//package com.fms.rest;
//
//import com.fms.entity.Users;
//import com.fms.service.UserServiceLocal;
//
//import jakarta.ejb.EJB;
//import jakarta.ws.rs.*;
//import jakarta.ws.rs.core.MediaType;
//import jakarta.ws.rs.core.Response;
//
//@Path("/users")
//public class UserRest {
//
//    @EJB
//    private UserServiceLocal userService;
//
//    // 🔐 LOGIN API
//    @POST
//    @Path("/login")
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response login(@FormParam("email") String email,
//                          @FormParam("password") String password) {
//
//        Users user = userService.login(email, password);
//
//        if (user != null) {
//            return Response.ok(user).build();
//        } else {
//            return Response.status(Response.Status.UNAUTHORIZED)
//                    .entity("Invalid email or password")
//                    .build();
//        }
//    }
//}


package com.fms.rest;

import com.fms.entity.Users;
import com.fms.service.UserServiceLocal;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.*;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserRest {

    @EJB
    private UserServiceLocal userService;

    // 🔐 LOGIN
    @POST
    @Path("/login")
    public Map<String, Object> login(Users user) {

        Users u = userService.login(user.getEmail(), user.getPassword());

        if (u == null) {
            throw new WebApplicationException("Invalid email or password", 401);
        }

        return convertToMap(u);
    }

    // ➕ CREATE USER
    @POST
    @Path("/create")
    public String createUser(Users user,
                             @QueryParam("roleId") int roleId,
                             @QueryParam("companyId") int companyId,
                             @QueryParam("branchId") Integer branchId) {

        userService.createUser(user, roleId, companyId, branchId);
        return "User created";
    }

    // 📋 GET USERS BY COMPANY
    @GET
    @Path("/company/{cid}")
    public List<Map<String, Object>> getUsersByCompany(@PathParam("cid") int cid) {

        List<Users> users = userService.getUsersByCompany(cid);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Users u : users) {
            result.add(convertToMap(u));
        }

        return result;
    }

    // 📋 GET USERS BY ROLE
    @GET
    @Path("/role/{rid}")
    public List<Map<String, Object>> getUsersByRole(@PathParam("rid") int rid) {

        List<Users> users = userService.getUsersByRole(rid);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Users u : users) {
            result.add(convertToMap(u));
        }

        return result;
    }

    // ✅ ACTIVATE USER
    @PUT
    @Path("/activate/{id}")
    public String activateUser(@PathParam("id") int id) {
        userService.activateUser(id);
        return "User activated";
    }

    // ❌ DEACTIVATE USER
    @PUT
    @Path("/deactivate/{id}")
    public String deactivateUser(@PathParam("id") int id) {
        userService.deactivateUser(id);
        return "User deactivated";
    }

    // 🔥 COMMON METHOD (IMPORTANT)
    private Map<String, Object> convertToMap(Users u) {

        Map<String, Object> map = new HashMap<>();

        map.put("uid", u.getUid());
        map.put("name", u.getName());
        map.put("email", u.getEmail());
        map.put("status", u.getStatus());

        if (u.getRid() != null) {
            map.put("role", u.getRid().getRoleName());
        }

        if (u.getCid() != null) {
            map.put("company", u.getCid().getCompanyName());
        }

        if (u.getBid() != null) {
            map.put("branch", u.getBid().getBranchName());
        }

        return map;
    }
}