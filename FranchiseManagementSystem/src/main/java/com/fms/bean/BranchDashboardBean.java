package com.fms.bean;

import com.fms.entity.Users;
import com.fms.service.InventoryServiceLocal;
import com.fms.service.SalesServiceLocal;
import com.fms.service.UserServiceLocal;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;

import java.io.Serializable;
import java.math.BigDecimal;
import com.fms.entity.Inventory;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class BranchDashboardBean
implements Serializable {

    @EJB
    private InventoryServiceLocal inventoryService;

    @EJB
    private UserServiceLocal userService;

    @EJB
    private SalesServiceLocal salesService;

    private Long totalProducts = 0L;

    private Long totalStaff = 0L;

    private Long lowStockCount = 0L;

    private BigDecimal todaySales =
            BigDecimal.ZERO;
    
    private List<Inventory> inventoryList =
        new ArrayList<>();

    @PostConstruct
    public void init() {

        try {

            HttpSession session =
                (HttpSession) FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getSession(false);

            if(session == null) {
                return;
            }

            Users user =
                (Users) session.getAttribute("user");

            if(user == null) {
                return;
            }

            int branchId =
                user.getBid().getBid();

            totalProducts =
                inventoryService
                    .getTotalProductsByBranch(
                        branchId
                    );

            totalStaff =
                userService
                    .getStaffCountByBranch(
                        branchId
                    );

            todaySales =
                salesService
                    .getTodaySales(
                        branchId
                    );

            lowStockCount =
                inventoryService
                    .getLowStockCount(
                        branchId
                    );
            
            inventoryList =
                inventoryService
                    .getRecentInventoryByBranch(
                        branchId
                    );  

        } catch(Exception e) {

            e.printStackTrace();
        }
    }

    public Long getTotalProducts() {
        return totalProducts;
    }

    public Long getTotalStaff() {
        return totalStaff;
    }

    public Long getLowStockCount() {
        return lowStockCount;
    }

    public BigDecimal getTodaySales() {
        return todaySales;
    }
    
    public String getFormattedTodaySales() {
        return formatAmount(todaySales);
    }
    
    public List<Inventory> getInventoryList() {
        return inventoryList;
    }
    
    public String formatAmount(BigDecimal amount) {

    if (amount == null) {
        return "₹0";
    }

    double value = amount.doubleValue();

    if (value >= 10000000) {
        return String.format("₹%.1fCr", value / 10000000);
    }
    else if (value >= 100000) {
        return String.format("₹%.1fL", value / 100000);
    }
    else if (value >= 1000) {
        return String.format("₹%.1fK", value / 1000);
    }

    return String.format("₹%.0f", value);
}
}