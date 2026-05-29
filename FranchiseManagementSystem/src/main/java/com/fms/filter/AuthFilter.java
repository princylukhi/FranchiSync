package com.fms.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter extends HttpFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req =
                (HttpServletRequest) request;

        HttpServletResponse res =
                (HttpServletResponse) response;

        HttpSession session =
                req.getSession(false);

        String path = req.getRequestURI();

        // PUBLIC PAGES
        boolean isPublicPage =
                path.contains("login.xhtml") ||
                path.contains("index.xhtml") ||
                path.contains("company_register.xhtml") ||
                path.contains("companies.xhtml") ||
                path.contains("/company-logo/") ||
                path.contains("apply_franchise.xhtml") ||
                path.contains("forgot-password.xhtml") ||
                path.contains("reset-password.xhtml") ||
                path.contains("jakarta.faces.resource") ||
                path.contains("/resources/");

        // CHECK LOGIN
        boolean loggedIn =
                session != null &&
                session.getAttribute("user") != null;

        // ALLOW PUBLIC PAGES
        if (isPublicPage || loggedIn) {

            chain.doFilter(request, response);

        } else {

            // REDIRECT TO LOGIN
            res.sendRedirect(
                    req.getContextPath() + "/login.xhtml"
            );
        }
    }
}