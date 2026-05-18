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
public class RoleFilter extends HttpFilter implements Filter {

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

        // If not logged in → skip
        if (session == null ||
                session.getAttribute("role") == null) {

            chain.doFilter(request, response);
            return;
        }

        String role =
                session.getAttribute("role").toString();

        // SYSTEM ADMIN
        if (path.contains("/admin/")
                && !role.equals("SYSTEM_ADMIN")) {

            res.sendRedirect(
                    req.getContextPath() + "/login.xhtml"
            );

            return;
        }

        // SUPER ADMIN
        if (path.contains("/company/")
                && !role.equals("SUPER_ADMIN")) {

            res.sendRedirect(
                    req.getContextPath() + "/login.xhtml"
            );

            return;
        }

        // FRANCHISE OWNER
        if (path.contains("/franchise/")
                && !role.equals("FRANCHISE_OWNER")) {

            res.sendRedirect(
                    req.getContextPath() + "/login.xhtml"
            );

            return;
        }

        // BRANCH MANAGER
        if (path.contains("/branch/")
                && !role.equals("BRANCH_MANAGER")) {

            res.sendRedirect(
                    req.getContextPath() + "/login.xhtml"
            );

            return;
        }

        // STAFF
        if (path.contains("/staff/")
                && !role.equals("STAFF")) {

            res.sendRedirect(
                    req.getContextPath() + "/login.xhtml"
            );

            return;
        }

        chain.doFilter(request, response);
    }
}