package com.fms.service;

import jakarta.ejb.Local;
import java.util.List;

@Local
public interface AdminReportServiceLocal {

    // USER ROLE DISTRIBUTION

    long getSystemAdminCount();

    long getCompanyAdminCount();

    long getFranchiseOwnerCount();

    long getBranchManagerCount();

    long getStaffCount();

    // FEEDBACK STAR COUNTS

    long getRatingCount(int rating);
    
    List<Object[]> getTopCompaniesByFranchiseCount();

        List<Object[]> getMonthlyCompanyRegistrations();

        List<Object[]> getWeeklyCompanyRegistrations();

        List<Object[]> getMonthlyFranchiseRegistrations();

        List<Object[]> getWeeklyFranchiseRegistrations();

        List<Object[]> getMonthlyUserRegistrations();

        List<Object[]> getWeeklyUserRegistrations();

}