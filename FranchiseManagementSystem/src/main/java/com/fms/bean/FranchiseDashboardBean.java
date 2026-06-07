package com.fms.bean;

import com.fms.entity.Users;
import com.fms.service.BranchServiceLocal;
import com.fms.service.RoyaltyServiceLocal;
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
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class FranchiseDashboardBean
implements Serializable {

    @EJB
    private BranchServiceLocal branchService;

    @EJB
    private UserServiceLocal userService;

    @EJB
    private RoyaltyServiceLocal royaltyService;
    
    @EJB
    private SalesServiceLocal salesService;

    private Long totalBranches = 0L;

    private Long totalManagers = 0L;

    private BigDecimal totalSales =
            BigDecimal.ZERO;

    private BigDecimal totalRoyalty =
            BigDecimal.ZERO;

    private List<Object[]> branchPerformance =
        new ArrayList<>();
    
    private String companyName;
    
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

            var franchise =
                user.getFranchisesCollection()
                    .iterator()
                    .next();

            int franchiseId =
                franchise.getFid();

            companyName =
                franchise.getCid()
                         .getCompanyName();

            totalBranches =
                branchService
                    .getTotalBranchesByFranchise(
                        franchiseId
                    );

            totalManagers =
                userService
                    .getTotalManagersByFranchise(
                        franchiseId
                    );

            totalSales =
                royaltyService
                    .getCurrentMonthSales(
                        franchiseId
                    );

            totalRoyalty =
                royaltyService
                    .getCurrentMonthRoyalty(
                        franchiseId
                    );
            
            branchPerformance =
                salesService
                    .getBranchPerformance(
                        franchiseId
                    );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    public String formatAmount(BigDecimal amount) {

        if (amount == null) {
            return "₹0";
        }

        double value = amount.doubleValue();

        if (value >= 10000000) { // Crore
            return String.format("₹%.1fCr", value / 10000000);
        }

        if (value >= 100000) { // Lakh
            return String.format("₹%.1fL", value / 100000);
        }

        if (value >= 1000) { // Thousand
            return String.format("₹%.1fK", value / 1000);
        }

        return String.format("₹%.2f", value);
    }

    public Long getTotalBranches() {
        return totalBranches;
    }

    public Long getTotalManagers() {
        return totalManagers;
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public BigDecimal getTotalRoyalty() {
        return totalRoyalty;
    }
    
    public List<Object[]> getBranchPerformance() {
        return branchPerformance;
    }
    
    public String getCompanyName() {
        return companyName;
    }
      
}