package com.fms.service;

import jakarta.ejb.Local;
import java.util.List;
import com.fms.entity.FranchiseRequests;
import com.fms.entity.Franchises;

@Local
public interface FranchiseServiceLocal {

    public void submitFranchiseRequest(FranchiseRequests request);

    public List<FranchiseRequests>
    getPendingRequests(int companyId);

    public void approveFranchise(int requestId);

    public void rejectFranchise(int requestId);

    public List<Franchises> getFranchisesByCompany(int cid);
    
    public long getActiveFranchiseCount();
    
    public List<FranchiseRequests>
    getRequestsByCompany(int companyId);
    
    public long getApprovedFranchiseCount(int companyId);

    public long getPendingFranchiseCount(int companyId);

    public List<FranchiseRequests> getRecentRequests(int companyId);
    
    public List<FranchiseRequests>
    getRequestsByStatus(int companyId, String status);
    
    public Franchises getFranchiseByOwner(int userId);
    
    List<Object[]> getMonthlyFranchiseRegistrations();

    List<Object[]> getWeeklyFranchiseRegistrations();
    
    List<Object[]> getMonthlyFranchiseGrowth(int companyId);

    List<Object[]> getBranchDistribution(int companyId);

    List<Object[]> getTopPerformingFranchises(int companyId);
    
   

}