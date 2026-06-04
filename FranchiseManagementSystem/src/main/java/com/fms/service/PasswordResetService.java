package com.fms.service;

import com.fms.entity.PasswordResetTokens;
import com.fms.entity.Users;
import jakarta.ejb.EJB;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Stateless
public class PasswordResetService
implements PasswordResetServiceLocal {

    @PersistenceContext(unitName = "FranchisePU")
    private EntityManager em;
    
    @EJB
    private EmailServiceLocal emailService;

    @Override
    public String generateToken() {

        return UUID.randomUUID().toString();
    }

    @Override
    public void createResetToken(Users user) {

        PasswordResetTokens token =
                new PasswordResetTokens();

        token.setUid(user);

        token.setToken(generateToken());

        Calendar cal =
                Calendar.getInstance();

        cal.add(Calendar.MINUTE, 5);

        token.setExpiryTime(
                cal.getTime()
        );

        token.setUsed(false);

        em.persist(token);
    }

    @Override
    public PasswordResetTokens
    findByToken(String tokenValue) {

        Query q =
                em.createNamedQuery(
                "PasswordResetTokens.findByToken"
        );

        q.setParameter(
                "token",
                tokenValue
        );

        List<PasswordResetTokens> list =
                q.getResultList();

        return list.isEmpty()
                ? null
                : list.get(0);
    }

    @Override
    public boolean isValidToken(
            String tokenValue) {

        PasswordResetTokens token =
                findByToken(tokenValue);

        if (token == null) {
            return false;
        }

        if (token.getUsed()) {
            return false;
        }

        return token
                .getExpiryTime()
                .after(new Date());
    }

    @Override
    public void markTokenUsed(
            String tokenValue) {

        PasswordResetTokens token =
                findByToken(tokenValue);

        if (token != null) {

            token.setUsed(true);

            em.merge(token);
        }
    }
    
    @Override
    public PasswordResetTokens findLatestByUser(int uid) {

        return em.createQuery(

            "SELECT p FROM PasswordResetTokens p "
          + "WHERE p.uid.uid = :uid "
          + "ORDER BY p.id DESC",

            PasswordResetTokens.class

        )
        .setParameter("uid", uid)
        .setMaxResults(1)
        .getSingleResult();
    }
}