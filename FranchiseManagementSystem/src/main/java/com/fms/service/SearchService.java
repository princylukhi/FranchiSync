package com.fms.service;

import com.fms.dto.SearchResult;
import com.fms.entity.Branches;
import com.fms.entity.Companies;
import com.fms.entity.Products;
import com.fms.entity.Users;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class SearchService
implements SearchServiceLocal {

    @PersistenceContext(unitName = "FranchisePU")
    private EntityManager em;

    @Override
   public List<SearchResult> globalSearch(
        String keyword,
        Users loggedUser) {
       
       

        List<SearchResult> results =
            new ArrayList<>();

        String search =
            "%" + keyword.toLowerCase() + "%";

        
        String role =
            loggedUser
            .getRid()
            .getRoleName();
        // Companies

        List<Companies> companies;

        if(role.equals("SYSTEM_ADMIN")) {

            companies =
                em.createQuery(
                    "SELECT c FROM Companies c " +
                    "WHERE LOWER(c.companyName) LIKE :s",
                    Companies.class)
                .setParameter("s", search)
                .getResultList();
        }
        else if(role.equals("SUPER_ADMIN")) {

            companies =
                em.createQuery(
                    "SELECT c FROM Companies c " +
                    "WHERE c.cid = :cid " +
                    "AND LOWER(c.companyName) LIKE :s",
                    Companies.class)
                .setParameter(
                    "cid",
                    loggedUser.getCid().getCid()
                )
                .setParameter("s", search)
                .getResultList();
        }
        else {

            companies =
                new ArrayList<>();
        }

        for (Companies c : companies) {

            results.add(

                new SearchResult(

                    "COMPANY",

                    c.getCompanyName(),

                    c.getBusinessType()
                )
            );
        }

        // Users

List<Users> users;

if(role.equals("SYSTEM_ADMIN")) {

    users =
        em.createQuery(
            "SELECT u FROM Users u " +
            "WHERE LOWER(u.name) LIKE :s " +
            "OR LOWER(u.email) LIKE :s",
            Users.class)
        .setParameter("s", search)
        .getResultList();
}
else if(role.equals("SUPER_ADMIN")) {

    users =
        em.createQuery(
            "SELECT u FROM Users u " +
            "WHERE u.cid.cid = :cid " +
            "AND (" +
            "LOWER(u.name) LIKE :s " +
            "OR LOWER(u.email) LIKE :s)",
            Users.class)
        .setParameter(
            "cid",
            loggedUser.getCid().getCid()
        )
        .setParameter("s", search)
        .getResultList();
}
else if(role.equals("FRANCHISE_OWNER")) {

    users =
        em.createQuery(

            "SELECT u FROM Users u " +
            "WHERE u.bid.fid.ownerUserId.uid = :uid " +
            "AND (" +
            "LOWER(u.name) LIKE :s " +
            "OR LOWER(u.email) LIKE :s)",

            Users.class)

        .setParameter(
            "uid",
            loggedUser.getUid()
        )

        .setParameter(
            "s",
            search
        )

        .getResultList();
}
else if(role.equals("BRANCH_MANAGER")) {

    users =
        em.createQuery(

            "SELECT u FROM Users u " +
            "WHERE u.bid.bid = :bid " +
            "AND (" +
            "LOWER(u.name) LIKE :s " +
            "OR LOWER(u.email) LIKE :s)",

            Users.class)

        .setParameter(
            "bid",
            loggedUser.getBid().getBid()
        )

        .setParameter(
            "s",
            search
        )

        .getResultList();
}
else {

    users =
        new ArrayList<>();
}

        for (Users u : users) {

            results.add(

                new SearchResult(

                    "USER",

                    u.getName(),

                    u.getRid().getRoleName()
                )
            );
        }

        // Branches

        List<Branches> branches;

if(role.equals("SYSTEM_ADMIN")) {

    branches =
        em.createQuery(
            "SELECT b FROM Branches b " +
            "WHERE LOWER(b.branchName) LIKE :s",
            Branches.class)
        .setParameter("s", search)
        .getResultList();
}
else if(role.equals("FRANCHISE_OWNER")) {

    branches =
        em.createQuery(

            "SELECT b FROM Branches b " +
            "WHERE b.fid.ownerUserId.uid = :uid " +
            "AND LOWER(b.branchName) LIKE :s",

            Branches.class)

        .setParameter(
            "uid",
            loggedUser.getUid()
        )

        .setParameter(
            "s",
            search
        )

        .getResultList();
}
else if(role.equals("BRANCH_MANAGER")) {

    branches =
        em.createQuery(

            "SELECT b FROM Branches b " +
            "WHERE b.bid = :bid " +
            "AND LOWER(b.branchName) LIKE :s",

            Branches.class)

        .setParameter(
            "bid",
            loggedUser.getBid().getBid()
        )

        .setParameter(
            "s",
            search
        )

        .getResultList();
}
else {

    branches =
        new ArrayList<>();
}

        for (Branches b : branches) {

            results.add(

                new SearchResult(

                    "BRANCH",

                    b.getBranchName(),

                    ""
                )
            );
        }

        // Products

        List<Products> products;

if(role.equals("SYSTEM_ADMIN")) {

    products =
        em.createQuery(
            "SELECT p FROM Products p " +
            "WHERE LOWER(p.productName) LIKE :s",
            Products.class)
        .setParameter("s", search)
        .getResultList();
}
else if(role.equals("SUPER_ADMIN")) {

    products =
        em.createQuery(
            "SELECT p FROM Products p " +
            "WHERE p.cid.cid = :cid " +
            "AND LOWER(p.productName) LIKE :s",
            Products.class)
        .setParameter(
            "cid",
            loggedUser.getCid().getCid()
        )
        .setParameter("s", search)
        .getResultList();
}
else if(role.equals("FRANCHISE_OWNER")) {

    products =
        em.createQuery(

            "SELECT p FROM Products p " +
            "WHERE p.cid.cid = :cid " +
            "AND LOWER(p.productName) LIKE :s",

            Products.class)

        .setParameter(
            "cid",
            loggedUser.getCid().getCid()
        )

        .setParameter(
            "s",
            search
        )

        .getResultList();
}
else if(role.equals("BRANCH_MANAGER")) {

    products =
        em.createQuery(

            "SELECT p FROM Products p " +
            "WHERE p.cid.cid = :cid " +
            "AND LOWER(p.productName) LIKE :s",

            Products.class)

        .setParameter(
            "cid",
            loggedUser.getCid().getCid()
        )

        .setParameter(
            "s",
            search
        )

        .getResultList();
}
else {

    products =
        new ArrayList<>();
}

        for (Products p : products) {

           results.add(

                new SearchResult(

                    "PRODUCT",

                    p.getProductName(),

                    ""
                )
            );
        }

        return results;
    }
}