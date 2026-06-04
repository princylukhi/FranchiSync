package com.fms.bean;

import com.fms.entity.Branches;
import com.fms.entity.Franchises;
import com.fms.entity.Users;

import com.fms.service.BranchServiceLocal;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import jakarta.servlet.http.HttpSession;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class FranchiseBranchBean implements Serializable {

    @EJB
    private BranchServiceLocal branchService;

    private List<Branches> branches =
            new ArrayList<>();

    private Branches branch =
            new Branches();

    private int franchiseId;

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

            Users user =
                    (Users) session.getAttribute("user");

            if (user == null) {
                return;
            }

            // FETCH FRANCHISE ID
            if (user.getFranchisesCollection() != null
                    && !user.getFranchisesCollection().isEmpty()) {

                franchiseId =
                        user.getFranchisesCollection()
                        .iterator()
                        .next()
                        .getFid();
            }

            loadBranches();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // LOAD BRANCHES

    public void loadBranches() {

        branches =
                branchService
                .getBranchesByFranchise(franchiseId);
    }

    // OPEN DIALOG

    public void openAddDialog() {

        branch = new Branches();
    }

    // ADD BRANCH

    public void addBranch() {

        try {

            Franchises f =
                    new Franchises();

            f.setFid(franchiseId);

            branch.setFid(f);
            
             // CONVERT TO UPPERCASE
            branch.setBranchCode(
                branch.getBranchCode().toUpperCase()
            );


            branchService.addBranch(branch);

            loadBranches();

            branch = new Branches();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // EDIT

    public void edit(Branches b) {

        branch = b;
    }

    // UPDATE

    public void updateBranch() {
        
        // CONVERT TO UPPERCASE
        branch.setBranchCode(
            branch.getBranchCode().toUpperCase()
        );


        branchService.updateBranch(branch);

        loadBranches();

        branch = new Branches();
    }

    // ACTIVATE

    public void activate(int bid) {

        branchService.activateBranch(bid);

        loadBranches();
    }

    // DEACTIVATE

    public void deactivate(int bid) {

        branchService.deactivateBranch(bid);

        loadBranches();
    }

    // GETTERS

    public List<Branches> getBranches() {
        return branches;
    }

    public void setBranches(List<Branches> branches) {
        this.branches = branches;
    }

    public Branches getBranch() {
        return branch;
    }

    public void setBranch(Branches branch) {
        this.branch = branch;
    }
}
