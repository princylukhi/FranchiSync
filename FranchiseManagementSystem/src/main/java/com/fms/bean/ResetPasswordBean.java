package com.fms.bean;

import com.fms.entity.PasswordResetTokens;
import com.fms.service.PasswordResetServiceLocal;
import com.fms.service.UserServiceLocal;
import com.fms.util.PasswordUtil;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@ViewScoped
public class ResetPasswordBean
implements Serializable {

    private String token;

    private String password;

    private String confirmPassword;

    private boolean validToken = false;

    @EJB
    private PasswordResetServiceLocal passwordResetService;

    @EJB
    private UserServiceLocal userService;

    @PostConstruct
    public void init() {

        token = FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap()
                .get("token");

        if(token != null) {

            validToken =
                    passwordResetService
                    .isValidToken(token);
        }
    }

    public void resetPassword() {

        try {

            if(!password.equals(confirmPassword)) {

                FacesContext.getCurrentInstance()
                        .addMessage(
                                null,
                                new FacesMessage(
                                        FacesMessage.SEVERITY_ERROR,
                                        "Error",
                                        "Passwords do not match"
                                ));

                return;
            }

            PasswordResetTokens prt =
                    passwordResetService
                    .findByToken(token);

            String hashedPassword =
                    PasswordUtil.hashPassword(password);

            userService.updatePassword(
                    prt.getUid().getUid(),
                    hashedPassword
            );

            passwordResetService
                    .markTokenUsed(token);

            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect(
                            "login.xhtml"
                    );

        } catch(Exception e) {

            e.printStackTrace();
        }
    }

    public String getToken() {
        return token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(
            String confirmPassword) {

        this.confirmPassword =
                confirmPassword;
    }

    public boolean isValidToken() {
        return validToken;
    }
}