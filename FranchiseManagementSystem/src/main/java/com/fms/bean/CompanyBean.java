package com.fms.bean;

import com.fms.entity.CompanyRegistrationRequests;
import com.fms.service.CompanyServiceLocal;

import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class CompanyBean implements Serializable{

    private CompanyRegistrationRequests request =
            new CompanyRegistrationRequests();

    @EJB
    private CompanyServiceLocal companyService;
    
    private String customBusinessType;

    // REGISTER COMPANY REQUEST
    public String submitRequest() {

        try {

           if ("Other".equals(request.getBusinessType())) {

    if (customBusinessType == null ||
        customBusinessType.trim().isEmpty()) {

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
            
            companyService.submitCompanyRequest(request);

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_INFO,
                            "Request Submitted",
                            "Your registration request is under review."

                    )
            );

            // Clear Form
            request = new CompanyRegistrationRequests();
            
            customBusinessType = null;

            return null;

        } catch (Exception e) {

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
}