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
    }

    // ADD STAFF

        public void addStaff() {

         try {

             // AUTO JOINING DATE
             staff.setJoiningDate(new Date());

             userService.createStaffUser(

                 staff,

                 loggedInUser.getCid().getCid(),

                 loggedInUser.getBid().getBid()
             );

             loadStaff();

             staff = new Users();

         } catch (Exception e) {

             e.printStackTrace();
         }
     }

    // EDIT

    public void edit(Users u) {

        staff = u;
    }

    // UPDATE

    public void updateStaff() {

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
}