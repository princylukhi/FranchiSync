package com.fms.bean;

import com.fms.entity.Sales;
import com.fms.entity.Users;
import com.fms.service.BranchServiceLocal;
import com.fms.service.SalesServiceLocal;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class FranchiseSalesBean
implements Serializable {

    @EJB
    private SalesServiceLocal salesService;

    @EJB
    private BranchServiceLocal branchService;

    private Users loggedInUser;

    private BigDecimal totalRevenue =
            BigDecimal.ZERO;

    private long totalOrders = 0L;

    private long activeBranches;

    private List<Sales> recentSales =
            new ArrayList<>();

    private List<Object[]> branchSales =
            new ArrayList<>();

    @PostConstruct
    public void init() {

        try {

            HttpSession session =
                (HttpSession) FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getSession(false);

            if(session == null) return;

            loggedInUser =
                (Users) session.getAttribute("user");

            if(loggedInUser == null) return;

            int franchiseId =
                loggedInUser
                .getFranchisesCollection()
                .iterator()
                .next()
                .getFid();

            totalRevenue =
                salesService
                .getTotalRevenueByFranchise(
                        franchiseId);

            totalOrders =
                salesService
                .getTotalOrdersByFranchise(
                        franchiseId);

            activeBranches =
                branchService.getActiveBranchesByFranchise(
                    franchiseId
                );

            recentSales =
                salesService
                .getRecentSalesByFranchise(
                        franchiseId);

            branchSales =
                salesService
                .getBranchWiseSales(
                        franchiseId);

        } catch(Exception e) {

            e.printStackTrace();
        }
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public Long getTotalOrders() {
        return totalOrders;
    }

    public List<Sales> getRecentSales() {
        return recentSales;
    }

    public List<Object[]> getBranchSales() {
        return branchSales;
    }
    
    public long getActiveBranches() {
        return activeBranches;
    }
    
}