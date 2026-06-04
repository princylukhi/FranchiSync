package com.fms.bean;

import com.fms.entity.CompanyRegistrationRequests;
import com.fms.service.CompanyServiceLocal;

import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import org.primefaces.model.file.UploadedFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Named
@ViewScoped
public class CompanyBean implements Serializable{

    private CompanyRegistrationRequests request =
            new CompanyRegistrationRequests();

    @EJB
    private CompanyServiceLocal companyService;
    
    private String customBusinessType;
    
    private UploadedFile logoFile;

    // REGISTER COMPANY REQUEST
    public String submitRequest() {

    try {

        // OTHER BUSINESS TYPE
        if ("Other".equals(request.getBusinessType())) {

            if (customBusinessType == null
                    || customBusinessType.trim().isEmpty()) {

                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(
                                FacesMessage.SEVERITY_ERROR,
                                "Business Type Required",
                                "Please enter custom business type."
                        )
                );

                return null;
            }

            request.setBusinessType(customBusinessType);
        }

        // =========================
        // LOGO UPLOAD FIRST
        // =========================

        try {

            if (logoFile != null) {

                String fileName =
                        System.currentTimeMillis()
                        + "_"
                        + logoFile.getFileName();

                String uploadPath =
                        System.getProperty("com.sun.aas.instanceRoot")
                        + File.separator
                        + "company-logos"
                        + File.separator;

                File folder = new File(uploadPath);

                if (!folder.exists()) {
                    folder.mkdirs();
                }

                Path path = Paths.get(uploadPath + fileName);

                Files.copy(
                        logoFile.getInputStream(),
                        path
                );

                // 🔥 IMPORTANT
                request.setLogo(fileName);

                System.out.println("LOGO SAVED : " + fileName);
            }

        } catch (IOException e) {

            e.printStackTrace();
        }

        // =========================
        // SAVE DATABASE AFTER LOGO
        // =========================

        companyService.submitCompanyRequest(request);

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                        FacesMessage.SEVERITY_INFO,
                        "Request Submitted",
                        "Your registration request is under review."
                )
        );

        // CLEAR FORM
        request = new CompanyRegistrationRequests();

        customBusinessType = null;

        logoFile = null;

        return null;

    } catch (Exception e) {

        e.printStackTrace();

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Error",
                        e.getMessage()
                )
        );

        return null;
    }
}
    // GETTER SETTER
    public CompanyRegistrationRequests getRequest() {
        return request;
    }

    public void setRequest(CompanyRegistrationRequests request) {
        this.request = request;
    }
    
    public String getCustomBusinessType() {
        return customBusinessType;
    }

    public void setCustomBusinessType(String customBusinessType) {
        this.customBusinessType = customBusinessType;
    }
    
    public UploadedFile getLogoFile() {
        return logoFile;
    }

    public void setLogoFile(UploadedFile logoFile) {
        this.logoFile = logoFile;
    }
}