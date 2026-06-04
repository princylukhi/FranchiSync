package com.fms.bean;

import com.fms.entity.Users;
import com.fms.service.UserServiceLocal;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class AdminUserBean implements Serializable {

    @EJB
    private UserServiceLocal userService;

    private List<Users> users;

    private String selectedStatus = "ALL";

    @PostConstruct
    public void init() {

        users = userService.getAllUsers();
    }

    // FILTER USERS
    public void filterUsers(String status) {

        selectedStatus = status;

        if(status.equals("ALL")) {

            users = userService.getAllUsers();

        } else {

            users =
                    userService.getUsersByStatus(status);
        }
    }

    // ACTIVATE
    public void activate(int uid) {

        userService.activateUser(uid);

        filterUsers(selectedStatus);
    }

    // DEACTIVATE
    public void deactivate(int uid) {

        userService.deactivateUser(uid);

        filterUsers(selectedStatus);
    }

    // GETTERS SETTERS
    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

    public String getSelectedStatus() {
        return selectedStatus;
    }

    public void setSelectedStatus(String selectedStatus) {
        this.selectedStatus = selectedStatus;
    }
}