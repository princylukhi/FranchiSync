package com.fms.bean;

import com.fms.dto.RoyaltyRow;
import com.fms.entity.Users;

import com.fms.service.BranchServiceLocal;
import com.fms.service.RoyaltyServiceLocal;

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
public class FranchiseRoyaltyBean
implements Serializable {

    @EJB
    private RoyaltyServiceLocal royaltyService;

    @EJB
    private BranchServiceLocal branchService;

    private BigDecimal totalSales =
            BigDecimal.ZERO;

    private BigDecimal totalRoyalty =
            BigDecimal.ZERO;

    private Long activeBranches = 0L;
    
    private Long totalBranches;

    private List<RoyaltyRow> royaltyRows =
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

            int franchiseId =
                user.getFranchisesCollection()
                    .iterator()
                    .next()
                    .getFid();

            totalSales =
                royaltyService
                .getTotalSales(franchiseId);

            totalRoyalty =
                royaltyService
                .getTotalRoyalty(franchiseId);

            totalBranches =
                branchService.getTotalBranchesByFranchise(franchiseId);

            royaltyRows =
                royaltyService
                .getRoyaltyReport(franchiseId);

        } catch(Exception e) {

            e.printStackTrace();
        }
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public BigDecimal getTotalRoyalty() {
        return totalRoyalty;
    }

    public Long getActiveBranches() {
        return activeBranches;
    }

    public List<RoyaltyRow> getRoyaltyRows() {
        return royaltyRows;
    }
    
    public Long getTotalBranches() {
        return totalBranches;
    }
}