package com.fms.service;

import jakarta.ejb.Local;
import java.util.List;
import com.fms.entity.Branches;

@Local
public interface BranchServiceLocal {

    public void addBranch(Branches branch);

    public void updateBranch(Branches branch);

    public List<Branches> getBranchesByFranchise(int fid);

    public void activateBranch(int branchId);

    public void deactivateBranch(int branchId);
    
    public List<Branches> getAvailableBranches(int franchiseId);
    
    public long getTotalBranchesByFranchise(int franchiseId);
    
    public String getBranchNameById(int branchId);
    
    List<Object[]> getStaffDistribution(int franchiseId);

    long getTotalStaffByFranchise(int franchiseId);

    List<Object[]> getTopPerformingBranches(int franchiseId);
    
    long getActiveBranchesByFranchise(int franchiseId);

}