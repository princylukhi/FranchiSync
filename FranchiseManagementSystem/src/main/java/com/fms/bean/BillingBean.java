package com.fms.bean;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fms.entity.Customers;
import com.fms.entity.Inventory;
import com.fms.entity.Products;
import com.fms.entity.Payments;
import com.fms.entity.Sales;
import com.fms.entity.Users;

import com.fms.service.BillingServiceLocal;
import com.fms.service.CustomerServiceLocal;
import com.fms.service.InventoryServiceLocal;
import com.fms.service.PaymentServiceLocal;

@Named
@ViewScoped
public class BillingBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @EJB
    private BillingServiceLocal billingService;

    @EJB
    private CustomerServiceLocal customerService;

    @EJB
    private InventoryServiceLocal inventoryService;

    @EJB
    private PaymentServiceLocal paymentService;

    @Inject
    private LoginBean loginBean;

    private Customers customer = new Customers();

    private String mobileNo;

    private Integer selectedProductId;

    private int quantity = 1;

    private String paymentMode = "Cash";

    private List<CartItem> cartItems;

    private List<Inventory> availableProducts;

    private BigDecimal grandTotal;


@PostConstruct
public void init() {

    cartItems = new ArrayList<>();

    grandTotal = BigDecimal.ZERO;

    loadAvailableProducts();
}

public void loadAvailableProducts() {

    Users staff = loginBean.getLoggedUser();

    availableProducts =
            inventoryService.getAvailableInventory(
                    staff.getBid().getBid()
            );
}

public void searchCustomer() {

    Customers existing =
            customerService.findByMobile(mobileNo);

    if (existing != null) {

        customer = existing;

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                        FacesMessage.SEVERITY_INFO,
                        "Customer Found",
                        "Customer details loaded successfully"
                )
        );

    } else {

        customer = new Customers();

        customer.setMobileNo(mobileNo);

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                        FacesMessage.SEVERITY_WARN,
                        "Customer Not Found",
                        "New customer. Please enter customer details."
                )
        );
    }
}

public void addToCart() {

    if (selectedProductId == null) {

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Error",
                        "Please select a product"
                )
        );

        return;
    }

    Inventory inventory =
            availableProducts.stream()
                    .filter(i -> i.getPid().getPid().equals(selectedProductId))
                    .findFirst()
                    .orElse(null);

    if (inventory == null) {
        return;
    }

    if (quantity <= 0) {

    FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Error",
                    "Quantity must be greater than zero"
            )
    );
    return;
}

if (quantity > inventory.getQuantity()) {

    FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Out of Stock",
                    "Only " + inventory.getQuantity() + " item(s) available"
            )
    );
    return;
}

    Products product = inventory.getPid();

    CartItem item = new CartItem();

    item.setProductId(product.getPid());
    item.setProductName(product.getProductName());
    item.setPrice(product.getPrice());
    item.setQuantity(quantity);

    item.setAmount(
            product.getPrice().multiply(
                    BigDecimal.valueOf(quantity)
            )
    );

    for (CartItem existing : cartItems) {

    if (existing.getProductId().equals(product.getPid())) {

        int newQty =
                existing.getQuantity() + quantity;

        if (newQty > inventory.getQuantity()) {

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Error",
                            "Stock exceeded"
                    )
            );

            return;
        }

        existing.setQuantity(newQty);

        existing.setAmount(
                existing.getPrice().multiply(
                        BigDecimal.valueOf(newQty)
                )
        );

        calculateGrandTotal();

        selectedProductId = null;
        quantity = 1;

        return;
    }
}
    
    cartItems.add(item);

    calculateGrandTotal();

    selectedProductId = null;
    quantity = 1;
}

public void removeItem(CartItem item) {

    cartItems.remove(item);

    calculateGrandTotal();
}

public void calculateGrandTotal() {

    grandTotal = BigDecimal.ZERO;

    for (CartItem item : cartItems) {

        grandTotal =
                grandTotal.add(
                        item.getAmount()
                );
    }
}

public void generateBill() {

    try {

        // Customer validation
       if (customer.getCustomerName() == null
        || customer.getCustomerName().trim().isEmpty()) {

    customer.setCustomerName("Walk-In Customer");
}
        // Cart validation
        if (cartItems.isEmpty()) {

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Error",
                            "Cart is empty"
                    )
            );

            return;
        }

        // Save customer if new

        customer.setMobileNo(mobileNo);
        customer = customerService.saveOrUpdate(customer);

        Users staff = loginBean.getLoggedUser();

        // Create Sale
        Sales sale = new Sales();

        sale.setCustomerId(customer);
        sale.setStaffId(staff);
        sale.setBid(staff.getBid());

        sale.setPaymentMode(paymentMode);
        sale.setStatus("COMPLETED");

        sale = billingService.createSale(sale);

        // Add cart items
        for (CartItem item : cartItems) {

            billingService.addSaleItem(
                    sale.getSid(),
                    item.getProductId(),
                    item.getQuantity()
            );
        }

        // Reload sale with updated total
        sale = billingService.findSaleById(
                sale.getSid()
        );

        // Generate Invoice
        billingService.completeSale(
                sale.getSid()
        );

        // Create Payment
        Payments payment = new Payments();

        payment.setSid(sale);

        payment.setAmountPaid(
                sale.getTotalAmount()
        );

        payment.setPaymentMode(
                paymentMode
        );

        payment.setPaymentStatus(
                "PAID"
        );

        paymentService.createPayment(
                payment
        );

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                        FacesMessage.SEVERITY_INFO,
                        "Success",
                        "Bill generated successfully"
                )
        );

        clearForm();

        loadAvailableProducts();

    } catch (Exception e) {

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Error",
                        e.getMessage()
                )
        );
    }
}

public void clearForm() {

    customer = new Customers();

    mobileNo = null;

    selectedProductId = null;

    quantity = 1;

    paymentMode = "Cash";

    cartItems.clear();

    grandTotal = BigDecimal.ZERO;
}

public Customers getCustomer() {
    return customer;
}

public void setCustomer(Customers customer) {
    this.customer = customer;
}

public String getMobileNo() {
    return mobileNo;
}

public void setMobileNo(String mobileNo) {
    this.mobileNo = mobileNo;
}

public Integer getSelectedProductId() {
    return selectedProductId;
}

public void setSelectedProductId(Integer selectedProductId) {
    this.selectedProductId = selectedProductId;
}

public int getQuantity() {
    return quantity;
}

public void setQuantity(int quantity) {
    this.quantity = quantity;
}

public String getPaymentMode() {
    return paymentMode;
}

public void setPaymentMode(String paymentMode) {
    this.paymentMode = paymentMode;
}

public List<CartItem> getCartItems() {
    return cartItems;
}

public void setCartItems(List<CartItem> cartItems) {
    this.cartItems = cartItems;
}

public List<Inventory> getAvailableProducts() {
    return availableProducts;
}

public void setAvailableProducts(List<Inventory> availableProducts) {
    this.availableProducts = availableProducts;
}

public BigDecimal getGrandTotal() {
    return grandTotal;
}

public void setGrandTotal(BigDecimal grandTotal) {
    this.grandTotal = grandTotal;
}
}

