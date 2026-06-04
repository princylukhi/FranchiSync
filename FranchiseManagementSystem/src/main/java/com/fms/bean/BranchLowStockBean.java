package com.fms.bean;

import com.fms.entity.Inventory;
import com.fms.entity.Users;
import com.fms.service.InventoryServiceLocal;

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
public class BranchLowStockBean
implements Serializable {

    @EJB
    private InventoryServiceLocal inventoryService;

    private List<Inventory> lowStockList =
            new ArrayList<>();

    private int branchId;

    @PostConstruct
    public void init() {

        HttpSession session =
            (HttpSession)
            FacesContext.getCurrentInstance()
            .getExternalContext()
            .getSession(false);

        if(session == null) {
            return;
        }

        Users user =
            (Users) session.getAttribute("user");

        if(user == null ||
           user.getBid() == null) {
            return;
        }

        branchId =
            user.getBid().getBid();

        loadLowStock();
    }

    public void loadLowStock() {

        lowStockList =
            inventoryService
            .getLowStock(branchId);
    }

    public String getStatus(
            Inventory inventory) {

        if(inventory.getQuantity() == 0) {

            return "OUT OF STOCK";
        }

        return "LOW STOCK";
    }

    public List<Inventory> getLowStockList() {
        return lowStockList;
    }
}