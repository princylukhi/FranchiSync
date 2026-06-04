package com.fms.bean;

import com.fms.dto.SearchResult;
import com.fms.service.SearchServiceLocal;

import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fms.entity.Users;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpSession;

@Named
@ViewScoped
public class SearchBean implements Serializable {

    @EJB
    private SearchServiceLocal searchService;

    private String keyword;

    private List<SearchResult> results =
        new ArrayList<>();

    public void search() {

        if (keyword == null ||
            keyword.trim().isEmpty()) {

            results =
                new ArrayList<>();

            return;
        }
        
        

        HttpSession session =
                (HttpSession) FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getSession(false);

            Users user =
                (Users) session.getAttribute("user");

            results =
                searchService
                .globalSearch(
                    keyword,
                    user
                );
    }
    

            public String getRedirectUrl(
                SearchResult result) {

            HttpSession session =
                (HttpSession) FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getSession(false);

            Users user =
                (Users) session.getAttribute("user");

            String role =
                user.getRid().getRoleName();

            // SYSTEM ADMIN

            if(role.equals("SYSTEM_ADMIN")) {

                switch(result.getType()) {

                   case "USER":
                        return "/admin/users.xhtml";

                    case "COMPANY":
                        return "/admin/companies.xhtml";

                    case "BRANCH":
                        return "/admin/reports.xhtml";

                    case "PRODUCT":
                        return "/admin/reports.xhtml";
                }
            }

            // SUPER ADMIN

            if(role.equals("SUPER_ADMIN")) {

                switch(result.getType()) {

                  case "USER":
                        return "/company/franchises.xhtml";

                    case "COMPANY":
                        return "/company/dashboard.xhtml";

                    case "BRANCH":
                        return "/company/franchises.xhtml";

                    case "PRODUCT":
                        return "/company/products.xhtml";
                }
            }

            // FRANCHISE OWNER

            if(role.equals("FRANCHISE_OWNER")) {

                switch(result.getType()) {

                   case "USER":
                        return "/franchise/managers.xhtml";

                    case "COMPANY":
                        return "/franchise/dashboard.xhtml";

                    case "BRANCH":
                        return "/franchise/branches.xhtml";

                    case "PRODUCT":
                        return "/franchise/reports.xhtml";
                }
            }

            // BRANCH MANAGER

            if(role.equals("BRANCH_MANAGER")) {

                switch(result.getType()) {

                    case "USER":
                        return "/branch/staff.xhtml";

                    case "COMPANY":
                        return "/branch/dashboard.xhtml";

                    case "BRANCH":
                        return "/branch/reports.xhtml";

                    case "PRODUCT":
                        return "/branch/inventory.xhtml";
                }
            }

            return "#";
        }
            
    public void clearSearch() {

        keyword = "";
        results = new ArrayList<>();
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<SearchResult> getResults() {
        return results;
    }

    public void setResults(
            List<SearchResult> results) {

        this.results = results;
    }
}