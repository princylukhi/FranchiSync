package com.fms.bean;

import com.fms.entity.Companies;
import com.fms.entity.Products;
import com.fms.entity.Users;
import com.fms.service.CompanyServiceLocal;
import com.fms.service.ProductServiceLocal;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.faces.context.FacesContext;

import jakarta.servlet.http.HttpSession;

import org.primefaces.model.file.UploadedFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



@Named
@ViewScoped
public class CompanyProductBean implements Serializable {

    @EJB
    private ProductServiceLocal productService;

    @EJB
    private CompanyServiceLocal companyService;

    private List<Products> products = new ArrayList<>();

    private Products product = new Products();

    private UploadedFile uploadedFile;

    private List<String> categories = new ArrayList<>();
    private String customCategory;

    private int companyId;

    // =========================
    // INIT
    // =========================

    @PostConstruct
    public void init() {

        try {

            HttpSession session =
                (HttpSession) FacesContext.getCurrentInstance()
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

            if (user.getCid() == null) {
                return;
            }

            companyId = user.getCid().getCid();

            // LOAD PRODUCTS
            loadProducts();

            // BUSINESS TYPE
            String businessType =
                companyService.getBusinessTypeByEmail(
                    user.getEmail()
                );

            System.out.println(businessType);

            // FOOD
            if (businessType != null &&
                businessType.toLowerCase().contains("food")) {

                categories = Arrays.asList(
                    "Pizza",
                    "Burger",
                    "Beverages",
                    "Desserts",
                    "Fast Food",
                    "Snacks",
                    "Other"
                );
            }

            // RETAIL
            else if (businessType != null &&
                     businessType.toLowerCase().contains("retail")) {

                categories = Arrays.asList(
                    "Clothing",
                    "Electronics",
                    "Shoes",
                    "Accessories",
                    "Other"
                );
            }

            // EDUCATION
            else if (businessType != null &&
                     businessType.toLowerCase().contains("education")) {

                categories = Arrays.asList(
                    "Books",
                    "Courses",
                    "Stationery",
                    "Training",
                    "Other"
                );
            }

            // HEALTHCARE
            else if (businessType != null &&
                     businessType.toLowerCase().contains("health")) {

                categories = Arrays.asList(
                    "Medicines",
                    "Supplements",
                    "Equipment",
                    "Other"
                );
            }

            // GYM & FITNESS
            else if (businessType != null &&
                     businessType.toLowerCase().contains("gym")) {

                categories = Arrays.asList(
                    "Protein",
                    "Gym Equipment",
                    "Workout Accessories",
                    "Membership Plans",
                    "Fitness Wear",
                    "Other"
                );
            }

            // SALON & BEAUTY
            else if (businessType != null &&
                     businessType.toLowerCase().contains("salon")) {

                categories = Arrays.asList(
                    "Hair Care",
                    "Skin Care",
                    "Beauty Products",
                    "Spa Services",
                    "Cosmetics",
                    "Other"
                );
            }

            // OTHER BUSINESS TYPE
            else {

                categories = Arrays.asList(
                    "General",
                    "Other"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================
    // LOAD PRODUCTS
    // =========================

    public void loadProducts() {

    products =
        productService.getProductsByCompany(companyId);
}

    // =========================
    // ADD PRODUCT
    // =========================

  public void addProduct() {

    try {

        // COMPANY
        Companies company = new Companies();

        company.setCid(companyId);

        product.setCid(company);

        // DEFAULT ACTIVE
        product.setIsActive(true);

        // =========================
        // IMAGE UPLOAD
        // =========================

        if (uploadedFile != null &&
            uploadedFile.getFileName() != null &&
            !uploadedFile.getFileName().isEmpty()) {

            String fileName =
                System.currentTimeMillis()
                + "_"
                + uploadedFile.getFileName();
            
//            String uploadPath =
//            "C:/Users/Public/Payara_Server/glassfish/domains/domain1/docroot/product-images/";

            String uploadPath =
        System.getProperty("com.sun.aas.instanceRoot")
        + File.separator
        + "product-images"
        + File.separator;
            
            File folder = new File(uploadPath);

            if (!folder.exists()) {
                folder.mkdirs();
            }

            File file =
                new File(folder, fileName);

            try (
                InputStream input =
                    uploadedFile.getInputStream();

                FileOutputStream output =
                    new FileOutputStream(file)
            ) {

                byte[] buffer = new byte[1024];

                int length;

                while ((length = input.read(buffer)) > 0) {

                    output.write(buffer, 0, length);
                }
            }

            // SAVE IMAGE NAME IN DATABASE
            product.setImage(fileName);
        }

        // CUSTOM CATEGORY
        if ("Other".equals(product.getCategory())
                && customCategory != null
                && !customCategory.isEmpty()) {

            product.setCategory(customCategory);
        }

                // SAVE PRODUCT
                productService.addProduct(product);

                // RELOAD
                loadProducts();

                // RESET
                product = new Products();

                uploadedFile = null;
                customCategory = null;

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    // =========================
    // EDIT
    // =========================

    public void edit(Products p) {

        this.product = p;
    }

    // =========================
    // UPDATE
    // =========================

    public void updateProduct() {

    try {

        // =========================
        // IMAGE UPDATE
        // =========================

        if (uploadedFile != null
                && uploadedFile.getFileName() != null
                && !uploadedFile.getFileName().isEmpty()) {

            String fileName =
                    System.currentTimeMillis()
                    + "_"
                    + uploadedFile.getFileName();

            String uploadPath =
                    System.getProperty("com.sun.aas.instanceRoot")
                    + File.separator
                    + "product-images"
                    + File.separator;

            File folder = new File(uploadPath);

            if (!folder.exists()) {
                folder.mkdirs();
            }

            File file = new File(folder, fileName);

            try (
                    InputStream input = uploadedFile.getInputStream();
                    FileOutputStream output =
                            new FileOutputStream(file)
            ) {

                byte[] buffer = new byte[1024];

                int length;

                while ((length = input.read(buffer)) > 0) {

                    output.write(buffer, 0, length);
                }
            }

            // SAVE NEW IMAGE
            product.setImage(fileName);
        }

        // CUSTOM CATEGORY
        if ("Other".equals(product.getCategory())
                && customCategory != null
                && !customCategory.isEmpty()) {

            product.setCategory(customCategory);
        }

        productService.updateProduct(product);

        loadProducts();

        product = new Products();

        uploadedFile = null;

        customCategory = null;

    } catch (Exception e) {

        e.printStackTrace();
    }
}
    // =========================
    // ACTIVATE
    // =========================

    public void activate(int pid) {

        productService.activateProduct(pid);

        loadProducts();
    }

    // =========================
    // DEACTIVATE
    // =========================

    public void deactivate(int pid) {

        productService.deactivateProduct(pid);

        loadProducts();
    }
    
    public void openAddDialog() {

    product = new Products();

    product.setIsActive(true);

    uploadedFile = null;

    customCategory = null;
}

    // =========================
    // GETTERS & SETTERS
    // =========================

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
    
    public String getCustomCategory() {
        return customCategory;
    }

    public void setCustomCategory(String customCategory) {
        this.customCategory = customCategory;
    }
}