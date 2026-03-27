package com.fms.service;

import jakarta.ejb.Local;
import java.util.List;
import com.fms.entity.FranchiseRequests;
import com.fms.entity.Franchises;

@Local
public interface FranchiseServiceLocal {

    public void submitFranchiseRequest(FranchiseRequests request);

    public List<FranchiseRequests> getPendingRequests();

    public void approveFranchise(int requestId, int ownerUserId);

    public void rejectFranchise(int requestId);

    public List<Franchises> getFranchisesByCompany(int cid);

}