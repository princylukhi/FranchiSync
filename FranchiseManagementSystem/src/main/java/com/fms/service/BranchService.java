package com.fms.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;

import com.fms.entity.Branches;
import jakarta.ejb.EJB;

@Stateless
public class BranchService implements BranchServiceLocal {

    @PersistenceContext(unitName = "FranchisePU")
    private EntityManager em;
    
    @EJB
    private NotificationServiceLocal notificationService;

    @Override
    public void addBranch(Branches branch) {

        branch.setStatus("ACTIVE");

        em.persist(branch);
    }

    @Override
    public void updateBranch(Branches branch) {

        em.merge(branch);
    }

    @Override
    public List<Branches> getBranchesByFranchise(int fid) {

        Query q = em.createQuery(
            "SELECT b FROM Branches b WHERE b.fid.fid = :fid");

        q.setParameter("fid", fid);

        return q.getResultList();
    }

    @Override
    public void activateBranch(int branchId) {

        Branches branch = em.find(Branches.class, branchId);

        branch.setStatus("ACTIVE");

        em.merge(branch);
        
        notificationService.sendNotification(

        branch.getFid().getOwnerUserId().getEmail(),

        "Branch Activated",

        "Branch '" + branch.getBranchName()
        + "' (" + branch.getBranchCode()
        + ") has been activated successfully.",

        "BRANCH_STATUS"
    );
    }

    @Override
    public void deactivateBranch(int branchId) {

        Branches branch = em.find(Branches.class, branchId);

        branch.setStatus("INACTIVE");

        em.merge(branch);
        
        notificationService.sendNotification(

        branch.getFid()
              .getOwnerUserId()
              .getEmail(),

        "Branch Deactivated",

        "Branch '" + branch.getBranchName()
        + "' (" + branch.getBranchCode()
        + ") has been deactivated.",

        "BRANCH_STATUS"
    );
    }
    
    @Override
        public List<Branches> getAvailableBranches(int franchiseId) {

            Query q = em.createQuery(

                "SELECT b FROM Branches b " +
                "WHERE b.fid.fid = :fid " +
                "AND b.status = 'ACTIVE' " +
                "AND b.bid NOT IN (" +
                "   SELECT u.bid.bid FROM Users u " +
                "   WHERE u.bid IS NOT NULL " +
                "   AND u.rid.roleName = 'BRANCH_MANAGER'" +
                ")"

            );

            q.setParameter("fid", franchiseId);

            return q.getResultList();
        }
        
        @Override
        public long getTotalBranchesByFranchise(int franchiseId) {

            return em.createQuery(

                "SELECT COUNT(b) FROM Branches b " +
                "WHERE b.fid.fid = :fid",

                Long.class

            )
            .setParameter("fid", franchiseId)
            .getSingleResult();
        }
        
        @Override
        public String getBranchNameById(int branchId) {

            Branches branch =
                em.find(Branches.class, branchId);

            return branch != null
                    ? branch.getBranchName()
                    : "Unknown Branch";
        }
        
        @Override
        public List<Object[]> getStaffDistribution(int franchiseId) {

            return em.createQuery(

                "SELECT b.branchName, COUNT(u) " +
                "FROM Branches b " +
                "LEFT JOIN b.usersCollection u " +
                "WHERE b.fid.fid = :fid " +
                "AND u.rid.roleName = 'STAFF' " +
                "GROUP BY b.branchName",

                Object[].class

            )
            .setParameter("fid", franchiseId)
            .getResultList();
        }
        
        @Override
        public long getTotalStaffByFranchise(int franchiseId) {

            return em.createQuery(

                "SELECT COUNT(u) " +
                "FROM Users u " +
                "WHERE u.bid.fid.fid = :fid " +
                "AND u.rid.roleName = 'STAFF'",

                Long.class

            )
            .setParameter("fid", franchiseId)
            .getSingleResult();
        }
        
        @Override
        public List<Object[]> getTopPerformingBranches(
                int franchiseId) {

            return em.createNativeQuery(

                "SELECT " +
                "b.branch_name, " +
                "(COUNT(DISTINCT u.uid) + COUNT(DISTINCT f.fid)) score " +
                "FROM branches b " +
                "LEFT JOIN users u ON b.bid = u.bid " +
                "LEFT JOIN feedbacks f ON b.bid = f.branch_id " +
                "WHERE b.fid = ? " +
                "GROUP BY b.branch_name " +
                "ORDER BY score DESC"

            )
            .setParameter(1, franchiseId)
            .getResultList();
        }
}