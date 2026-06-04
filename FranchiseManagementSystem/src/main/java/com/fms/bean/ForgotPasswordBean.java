package com.fms.bean;

import com.fms.entity.PasswordResetTokens;
import com.fms.entity.Users;

import com.fms.service.EmailServiceLocal;
import com.fms.service.NotificationServiceLocal;
import com.fms.service.PasswordResetServiceLocal;
import com.fms.service.UserServiceLocal;

import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@ViewScoped
public class ForgotPasswordBean
implements Serializable {

    private String email;

    @EJB
    private UserServiceLocal userService;

    @EJB
    private PasswordResetServiceLocal
            passwordResetService;

    @EJB
    private EmailServiceLocal emailService;
    
    @EJB
    private NotificationServiceLocal notificationService;

    public void sendResetLink() {

        try {

            Users user =
                    userService
                    .findUserByEmail(email);

            if (user == null) {

                FacesContext
                        .getCurrentInstance()
                        .addMessage(
                                null,
                                new FacesMessage(
                                        FacesMessage.SEVERITY_ERROR,
                                        "Error",
                                        "Email not found"
                                )
                        );

                return;
            }

            passwordResetService
                    .createResetToken(user);

            PasswordResetTokens token =
                    passwordResetService
                    .findLatestByUser(
                            user.getUid()
                    );

//            String resetLink =
//
//                    "http://localhost:8081/"
//                    + "FranchiseManagementSystem/"
//                    + "reset-password.xhtml"
//                    + "?token="
//                    + token.getToken();

            String resetLink =
                        "http://192.168.43.153:8081/FranchiseManagementSystem/reset-password.xhtml?token="
                        + token.getToken();

            emailService.sendEmail(

                    user.getEmail(),

                    "Reset Password",

                    "Dear User,\n\n"

                    + "Click the link below "
                    + "to reset your password:\n\n"

                    + resetLink

                    + "\n\n"

                    + "This link is valid "
                    + "for 5 minutes.\n\n"

                    + "Regards,\n"
                    + "FranchiSync Team"
            );
            
            notificationService.sendNotification(

                user.getEmail(),

                "Password Reset Requested",

                "Password reset link generated for "
                + user.getName()
                + " ("
                + user.getEmail()
                + "). The reset link is valid for 5 minutes.",

                "PASSWORD_RESET"
            );
                

            FacesContext
                    .getCurrentInstance()
                    .addMessage(
                            null,
                            new FacesMessage(
                                    FacesMessage.SEVERITY_INFO,
                                    "Success",
                                    "Password reset link sent"
                            )
                    );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}