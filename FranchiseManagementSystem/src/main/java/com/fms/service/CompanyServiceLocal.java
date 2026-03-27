package com.fms.service;

import jakarta.ejb.Local;
import java.util.List;
import com.fms.entity.CompanyRegistrationRequests;

@Local
public interface CompanyServiceLocal {

    public void submitCompanyRequest(CompanyRegistrationRequests request);

    public List<CompanyRegistrationRequests> getPendingRequests();

    public void approveCompany(int requestId);

    public void rejectCompany(int requestId);

}