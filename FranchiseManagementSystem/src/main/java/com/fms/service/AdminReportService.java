package com.fms.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class AdminReportService
implements AdminReportServiceLocal {

    @PersistenceContext(unitName = "FranchisePU")
    private EntityManager em;
    
    @EJB
    private CompanyServiceLocal companyService;

    @EJB
    private FranchiseServiceLocal franchiseService;

    @EJB
    private UserServiceLocal userService;

    @Override
    public long getSystemAdminCount() {

        return em.createQuery(

            "SELECT COUNT(u) FROM Users u " +
            "WHERE u.rid.roleName='SYSTEM_ADMIN'",

            Long.class

        ).getSingleResult();
    }

    @Override
    public long getCompanyAdminCount() {

        return em.createQuery(

            "SELECT COUNT(u) FROM Users u " +
            "WHERE u.rid.roleName='SUPER_ADMIN'",

            Long.class

        ).getSingleResult();
    }

    @Override
    public long getFranchiseOwnerCount() {

        return em.createQuery(

            "SELECT COUNT(u) FROM Users u " +
            "WHERE u.rid.roleName='FRANCHISE_OWNER'",

            Long.class

        ).getSingleResult();
    }

    @Override
    public long getBranchManagerCount() {

        return em.createQuery(

            "SELECT COUNT(u) FROM Users u " +
            "WHERE u.rid.roleName='BRANCH_MANAGER'",

            Long.class

        ).getSingleResult();
    }

    @Override
    public long getStaffCount() {

        return em.createQuery(

            "SELECT COUNT(u) FROM Users u " +
            "WHERE u.rid.roleName='STAFF'",

            Long.class

        ).getSingleResult();
    }

    @Override
    public long getRatingCount(int rating) {

        return em.createQuery(

            "SELECT COUNT(f) FROM Feedbacks f " +
            "WHERE f.rating=:rating",

            Long.class

        )
        .setParameter("rating", rating)
        .getSingleResult();
    }
    
    @Override
    public List<Object[]> getTopCompaniesByFranchiseCount() {
        return companyService.getTopCompaniesByFranchiseCount();
    }

    @Override
    public List<Object[]> getMonthlyCompanyRegistrations() {
        return companyService.getMonthlyCompanyRegistrations();
    }

    @Override
    public List<Object[]> getWeeklyCompanyRegistrations() {
        return companyService.getWeeklyCompanyRegistrations();
    }

    @Override
    public List<Object[]> getMonthlyFranchiseRegistrations() {
        return franchiseService.getMonthlyFranchiseRegistrations();
    }

    @Override
    public List<Object[]> getWeeklyFranchiseRegistrations() {
        return franchiseService.getWeeklyFranchiseRegistrations();
    }

    @Override
    public List<Object[]> getMonthlyUserRegistrations() {
        return userService.getMonthlyUserRegistrations();
    }

    @Override
    public List<Object[]> getWeeklyUserRegistrations() {
        return userService.getWeeklyUserRegistrations();
    }
}