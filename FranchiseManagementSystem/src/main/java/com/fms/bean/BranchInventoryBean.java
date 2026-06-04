package com.fms.bean;

import com.fms.entity.Branches;
import com.fms.entity.Inventory;
import com.fms.entity.Products;
import com.fms.entity.Users;
import com.fms.service.InventoryServiceLocal;
import com.fms.service.ProductServiceLocal;

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
public class BranchInventoryBean
implements Serializable {

    @EJB
    private InventoryServiceLocal inventoryService;
    @EJB
    private ProductServiceLocal productService;

    private List<Products> products =
            new ArrayList<>();

    private Inventory newInventory =
            new Inventory();

    private int companyId;

    private List<Inventory> inventoryList =
            new ArrayList<>();

    private Inventory selectedInventory =
            new Inventory();

    private int branchId;
    
    private Integer selectedProductId;



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
        
        companyId =
    user.getBid()
        .getFid()
        .getCid()
        .getCid();

products =
    productService
    .getActiveProductsByCompany(
            companyId
    );

        loadInventory();
    }
    
    public void addInventory() {

        Products product =
        new Products();

product.setPid(
        selectedProductId
);

newInventory.setPid(
        product
);
    Inventory existing =

        inventoryService
        .getInventoryByProductAndBranch(

            newInventory
            .getPid()
            .getPid(),

            branchId
        );

    if(existing != null) {

        existing.setQuantity(

            existing.getQuantity()
            + newInventory.getQuantity()
        );

        inventoryService
                .updateStock(existing);

    } else {

        Branches branch =
                new Branches();

        branch.setBid(branchId);

        newInventory.setBid(branch);

        newInventory.setStatus("ACTIVE");

        inventoryService
                .addStock(newInventory);
    }

    loadInventory();

    newInventory =
            new Inventory();
}

    public void loadInventory() {

        inventoryList =
            inventoryService
            .getInventoryByBranch(branchId);
    }

    public void edit(Inventory inventory) {

        selectedInventory = inventory;
    }

    public void updateInventory() {

        inventoryService
            .updateStock(selectedInventory);

        loadInventory();

        selectedInventory =
            new Inventory();
    }

   public String getStockStatus(
        Inventory inventory) {

    if(inventory.getQuantity() == 0) {

        return "OUT OF STOCK";
    }

    if(inventory.getQuantity()
            <= inventory.getMinThreshold()) {

        return "LOW STOCK";
    }

    return "IN STOCK";
}

    public List<Inventory> getInventoryList() {
        return inventoryList;
    }

    public Inventory getSelectedInventory() {
        return selectedInventory;
    }

    public void setSelectedInventory(
            Inventory selectedInventory) {

        this.selectedInventory =
                selectedInventory;
    }
    
    public Inventory getNewInventory() {
        return newInventory;
    }

    public void setNewInventory(
            Inventory newInventory) {

        this.newInventory =
                newInventory;
    }

    public List<Products> getProducts() {
        return products;
    }

    public Integer getSelectedProductId() {
        return selectedProductId;
    }

    public void setSelectedProductId(
            Integer selectedProductId) {

        this.selectedProductId =
                selectedProductId;
    }
}