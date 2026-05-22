package com.fms.service;

import jakarta.ejb.Local;
import java.util.List;
import com.fms.entity.Companies;
import com.fms.entity.CompanyRegistrationRequests;

@Local
public interface CompanyServiceLocal {

    public void submitCompanyRequest(CompanyRegistrationRequests request);

    public List<CompanyRegistrationRequests> getPendingRequests();

    public void approveCompany(int requestId);

    public void rejectCompany(int requestId);
    
    public List<Companies> getAllCompanies();
    
    public List<CompanyRegistrationRequests> getAllRequests();
    
    public List<CompanyRegistrationRequests>
    getCompaniesByStatus(String status);

    public String getBusinessTypeByEmail(String email);
    
    public long getTotalCompanies();

    public long getPendingRequestCount();

}