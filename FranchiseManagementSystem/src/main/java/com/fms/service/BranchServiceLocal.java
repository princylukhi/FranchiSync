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

}