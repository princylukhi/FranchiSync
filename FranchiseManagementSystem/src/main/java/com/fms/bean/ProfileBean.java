package com.fms.bean;

import com.fms.entity.Users;
import com.fms.service.UserServiceLocal;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Named
@ViewScoped
public class ProfileBean implements Serializable {

    @EJB
    private UserServiceLocal userService;

    @Inject
    private LoginBean loginBean;

    private Users user;

    @PostConstruct
    public void init() {

        user = loginBean.getLoggedUser();

    }

    // UPDATE PROFILE
    public void updateProfile() {

        try {

            userService.updateUser(user);

            // update session user also
            loginBean.setLoggedUser(user);

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_INFO,
                            "Success",
                            "Profile updated successfully."
                    )
            );

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
    
    public void uploadPhoto(FileUploadEvent event) {
    try {
        UploadedFile file = event.getFile();
        if (file != null) {

            String fileName = System.currentTimeMillis()
                    + "_" + file.getFileName();

            String uploadDir = System.getProperty(
                    "com.sun.aas.instanceRoot")
                    + File.separator + "uploads"
                    + File.separator;

            File folder = new File(uploadDir);  // ✅
            if (!folder.exists()) {
                folder.mkdirs();
            }

            Path path = Paths.get(uploadDir + fileName);  // ✅
            Files.copy(
                    file.getInputStream(),
                    path,
                    StandardCopyOption.REPLACE_EXISTING
            );

            user.setProfilePhoto(fileName);
            userService.updateUser(user);
            loginBean.setLoggedUser(user);

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_INFO,
                            "Success",
                            "Profile photo updated."
                    )
            );
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    // COMPANY NAME
    public String getCompanyName() {

        if (user != null && user.getCid() != null) {

            return user.getCid().getCompanyName();
        }

        return "System";
    }

    // PROFILE IMAGE
    public String getProfileImage() {
    if (user != null
            && user.getProfilePhoto() != null
            && !user.getProfilePhoto().isEmpty()) {
        
        String contextPath = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestContextPath();
        
        return contextPath
        + "/uploads/"
        + user.getProfilePhoto()
        + "?t=" + System.currentTimeMillis();
    }
    return null;
}

    // GETTER SETTER
    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}