package com.fms.bean;

import com.fms.entity.CompanyRegistrationRequests;
import com.fms.service.CompanyServiceLocal;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.Date;

@Named
@RequestScoped
public class CompanyBean implements Serializable {

    // 🔥 FORM OBJECT
    private CompanyRegistrationRequests request =
            new CompanyRegistrationRequests();

    @EJB
    private CompanyServiceLocal companyService;

    // 🔥 SUBMIT REGISTRATION REQUEST
    public String submitRequest() {

        try {

            // Default Values
            request.setStatus("PENDING");
            request.setRequestDate(new Date());

            // Save Request
            companyService.submitCompanyRequest(request);

            // Success Message
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_INFO,
                            "Success",
                            "Company registration request submitted successfully"
                    )
            );

            // Reset Form
            request = new CompanyRegistrationRequests();

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

    // ===== GETTER & SETTER =====

    public CompanyRegistrationRequests getRequest() {
        return request;
    }

    public void setRequest(CompanyRegistrationRequests request) {
        this.request = request;
    }
}