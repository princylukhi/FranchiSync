package com.fms.bean;

import com.fms.entity.Users;
import com.fms.service.UserServiceLocal;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;

import java.io.Serializable;

@Named
@SessionScoped
public class LoginBean implements Serializable {

    private String email;
    private String password;

    private Users loggedUser;

    // 🔥 Dynamic Sidebar
    private String sidebarPage;

    @EJB
    private UserServiceLocal userService;

    // 🔐 LOGIN METHOD
    public String login() {

        Users user = userService.login(email, password);

        if (user != null && user.getRid() != null) {

            this.loggedUser = user;

            // 🔥 Create Session
            HttpSession session = (HttpSession)
                    FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSession(true);

            session.setAttribute("user", user);
            session.setAttribute("role", user.getRid().getRoleName());

            // Avoid null company error
            if (user.getCid() != null) {
                session.setAttribute("companyId",
                        user.getCid().getCid());
            }

            // 🔥 Role
            String role = user.getRid().getRoleName();

            // 🔥 Dynamic Sidebar
            if (role.equals("SYSTEM_ADMIN")) {

                sidebarPage = "/templates/sidebars/admin_sidebar.xhtml";

                return "/admin/dashboard.xhtml?faces-redirect=true";

            } else if (role.equals("SUPER_ADMIN")) {

                sidebarPage = "/templates/sidebars/company_sidebar.xhtml";

                return "/company/dashboard.xhtml?faces-redirect=true";

            } else if (role.equals("FRANCHISE_OWNER")) {

                sidebarPage = "/templates/sidebars/franchise_sidebar.xhtml";

                return "/franchise/dashboard.xhtml?faces-redirect=true";

            } else if (role.equals("BRANCH_MANAGER")) {

                sidebarPage = "/templates/sidebars/branch_sidebar.xhtml";

                return "/branch/dashboard.xhtml?faces-redirect=true";

            } else if (role.equals("STAFF")) {

                sidebarPage = "/templates/sidebars/staff_sidebar.xhtml";

                return "/staff/dashboard.xhtml?faces-redirect=true";
            }
        }

        // ❌ Invalid Login
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Login Failed",
                        "Invalid email or password"
                )
        );

        return null;
    }

    // 🚪 LOGOUT
    public String logout() {

        HttpSession session = (HttpSession)
                FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSession(false);

        if (session != null) {
            session.invalidate();
        }

        loggedUser = null;
        sidebarPage = null;

        return "/login.xhtml?faces-redirect=true";
    }
    
    // 🔔 DYNAMIC NOTIFICATION PAGE
    public String getNotificationPage() {

        String role = loggedUser.getRid().getRoleName();

        if(role.equals("SYSTEM_ADMIN")) {
            return "/admin/notifications.xhtml";
        }
        else if(role.equals("SUPER_ADMIN")) {
            return "/company/notifications.xhtml";
        }
        else if(role.equals("FRANCHISE_OWNER")) {
            return "/franchise/notifications.xhtml";
        }
        else if(role.equals("BRANCH_MANAGER")) {
            return "/branch/notifications.xhtml";
        }
        else {
            return "/staff/notifications.xhtml";
        }
    }

    // ===== GETTERS & SETTERS =====

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Users getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(Users loggedUser) {
        this.loggedUser = loggedUser;
    }

    public String getSidebarPage() {
        return sidebarPage;
    }

    public void setSidebarPage(String sidebarPage) {
        this.sidebarPage = sidebarPage;
    }
}