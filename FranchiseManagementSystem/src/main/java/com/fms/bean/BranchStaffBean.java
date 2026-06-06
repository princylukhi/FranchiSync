package com.fms.bean;

import com.fms.entity.Users;
import com.fms.service.NotificationServiceLocal;
import com.fms.service.UserServiceLocal;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
public class BranchStaffBean implements Serializable {

    @EJB
    private UserServiceLocal userService;

    @EJB
    private NotificationServiceLocal notificationService;

    private List<Users> staffList =
            new ArrayList<>();

    private Users staff =
            new Users();

    private Users loggedInUser;
    
    private String otherDesignation;

    @PostConstruct
    public void init() {

        HttpSession session =
            (HttpSession) FacesContext
            .getCurrentInstance()
            .getExternalContext()
            .getSession(false);

        loggedInUser =
            (Users) session.getAttribute("user");

        loadStaff();
    }

    // LOAD STAFF

    public void loadStaff() {

        int branchId =
            loggedInUser.getBid().getBid();

        staffList =
            userService.getStaffByBranch(branchId);
    }

    // OPEN DIALOG

    public void openAddDialog() {

        staff = new Users();
        otherDesignation = null;

    }

    // ADD STAFF

        public void addStaff() {

         try {

             if ("Other".equals(staff.getDesignation())
                    && otherDesignation != null
                    && !otherDesignation.trim().isEmpty()) {

                staff.setDesignation(otherDesignation.trim());
            }
             
             
             userService.createStaffUser(

                 staff,

                 loggedInUser.getCid().getCid(),

                 loggedInUser.getBid().getBid()
             );

             loadStaff();

             staff = new Users();
             otherDesignation = null;

         } catch (Exception e) {

             e.printStackTrace();
         }
     }

    // EDIT

    public void edit(Users u) {

        staff = u;

        List<String> predefined = List.of(
            "Sales Executive",
            "Cashier",
            "Store Assistant",
            "Team Member"
        );

        if (!predefined.contains(staff.getDesignation())) {

            otherDesignation = staff.getDesignation();

            staff.setDesignation("Other");
        }
        else {

            otherDesignation = null;
        }
    }

    // UPDATE

    public void updateStaff() {
        
        if ("Other".equals(staff.getDesignation())
        && otherDesignation != null
        && !otherDesignation.trim().isEmpty()) {

            staff.setDesignation(otherDesignation.trim());
        }

        userService.updateUser(staff);

        loadStaff();

        staff = new Users();
    }

    // ACTIVATE

    public void activate(int uid) {

        userService.activateUser(uid);

        loadStaff();
    }

    // DEACTIVATE

    public void deactivate(int uid) {

        userService.deactivateUser(uid);

        loadStaff();
    }

    // GETTERS

    public List<Users> getStaffList() {
        return staffList;
    }

    public Users getStaff() {
        return staff;
    }

    public void setStaff(Users staff) {
        this.staff = staff;
    }
    
    public String getOtherDesignation() {
        return otherDesignation;
    }

    public void setOtherDesignation(String otherDesignation) {
        this.otherDesignation = otherDesignation;
    }
}