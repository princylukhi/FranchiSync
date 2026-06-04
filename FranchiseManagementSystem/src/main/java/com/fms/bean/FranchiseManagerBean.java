package com.fms.bean;

import com.fms.entity.Branches;
import com.fms.entity.Users;
import com.fms.service.BranchServiceLocal;
import com.fms.service.NotificationServiceLocal;
import com.fms.service.UserServiceLocal;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class FranchiseManagerBean implements Serializable {

    @EJB
    private UserServiceLocal userService;

    @EJB
    private BranchServiceLocal branchService;

    @EJB
    private NotificationServiceLocal notificationService;

    private List<Users> managers = new ArrayList<>();

    private List<Branches> branches = new ArrayList<>();

    private Users manager = new Users();

    private Integer selectedBranchId;

    private Users loggedInUser;

   @PostConstruct
        public void init() {

            try {

                HttpSession session =
                        (HttpSession) FacesContext
                                .getCurrentInstance()
                                .getExternalContext()
                                .getSession(false);

                if (session == null) {
                    return;
                }

                loggedInUser =
                        (Users) session.getAttribute("user");

                if (loggedInUser == null) {
                    return;
                }

                loadManagers();
                loadBranches();

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

    // LOAD MANAGERS

    public void loadManagers() {

        int companyId =
            loggedInUser.getCid().getCid();

        managers =
            userService.getManagersByCompany(companyId);
    }

    // LOAD BRANCHES

    public void loadBranches() {

            branches = new ArrayList<>();

            if (loggedInUser == null) {
                return;
            }

            if (loggedInUser.getFranchisesCollection() == null
                    || loggedInUser.getFranchisesCollection().isEmpty()) {

                return;
            }

            int franchiseId =
                    loggedInUser.getFranchisesCollection()
                            .iterator()
                            .next()
                            .getFid();

            branches =
                    branchService.getAvailableBranches(franchiseId);
        }

    // OPEN DIALOG

    public void openAddDialog() {

        manager = new Users();

        selectedBranchId = null;
    }

    // ADD MANAGER

    public void addManager() {

        try {

            // CHECK EXISTING MANAGER

            if(userService.branchHasManager(selectedBranchId)) {

                FacesContext.getCurrentInstance()
                    .addMessage(null,
                    new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Manager already assigned to this branch",
                        null
                    ));

                return;
            }

            manager.setPassword("manager123");

            userService.createUser(
                manager,
                4,
                loggedInUser.getCid().getCid(),
                selectedBranchId
            );

            // SEND MAIL

            notificationService.sendBranchManagerCredentials(
                manager.getEmail(),
                "manager123"
            );
            
            Branches assignedBranch = branches.stream()
                .filter(b -> b.getBid().equals(selectedBranchId))
                .findFirst()
                .orElse(null);

            String branchName =
                assignedBranch != null
                ? assignedBranch.getBranchName()
                : "Assigned Branch";

            notificationService.sendNotification(

                manager.getEmail(),

                "Branch Manager Assigned",

                "Dear " + manager.getName() + ",\n\n"
                + "You have been assigned as Branch Manager for "
                + branchName + ".\n\n"
                + "You can now manage branch operations through FranchiSync.",

                "MANAGER_ASSIGNMENT"
            );

            loadManagers();

            manager = new Users();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // EDIT

    public void edit(Users u) {

        manager = u;

        if(u.getBid() != null) {

            selectedBranchId =
                u.getBid().getBid();
        }
    }

    // UPDATE

    public void updateManager() {

        userService.updateUser(manager);

        loadManagers();

        manager = new Users();
    }

    // ACTIVATE

    public void activate(int uid) {

        userService.activateUser(uid);

        loadManagers();
    }

    // DEACTIVATE

    public void deactivate(int uid) {

        userService.deactivateUser(uid);

        loadManagers();
    }

    // GETTERS & SETTERS

    public List<Users> getManagers() {
        return managers;
    }

    public Users getManager() {
        return manager;
    }

    public void setManager(Users manager) {
        this.manager = manager;
    }

    public List<Branches> getBranches() {
        return branches;
    }

    public Integer getSelectedBranchId() {
        return selectedBranchId;
    }

    public void setSelectedBranchId(Integer selectedBranchId) {
        this.selectedBranchId = selectedBranchId;
    }
}