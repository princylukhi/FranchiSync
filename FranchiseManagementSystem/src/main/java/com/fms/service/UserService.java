package com.fms.service;

import com.fms.entity.Users;
import com.fms.entity.Roles;
import com.fms.entity.Companies;
import com.fms.entity.Branches;
import com.fms.util.PasswordUtil;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.ejb.EJB;

import java.util.Date;
import java.util.List;

@Stateless
public class UserService implements UserServiceLocal {

    @PersistenceContext(unitName = "FranchisePU")
    private EntityManager em;

//    @EJB
//    private EmailServiceLocal emailService;
    
    @EJB
    private NotificationServiceLocal notificationService;

    @Override
    public void createUser(Users user, int roleId, int companyId, Integer branchId) {

        String plainPassword = user.getPassword();   // ✅ store original

        Roles role = em.find(Roles.class, roleId);
        Companies company = em.find(Companies.class, companyId);

        user.setRid(role);
        user.setCid(company);
        user.setCreatedDate(new Date());
        user.setStatus("ACTIVE");

        if (branchId != null) {
            Branches branch = em.find(Branches.class, branchId);
            user.setBid(branch);
        }

        // 🔐 Encrypt password
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));

        em.persist(user);

        // 📧 Send plain password to user
        notificationService.sendCredentials(user.getEmail(), plainPassword);
    }
    
    
    // 2️⃣ Get users by company
    @Override
    public List<Users> getUsersByCompany(int companyId) {

        Query q = em.createQuery(
                "SELECT u FROM Users u WHERE u.cid.cid = :cid"
        );

        q.setParameter("cid", companyId);

        return q.getResultList();
    }

    // 3️⃣ Get users by role
    @Override
    public List<Users> getUsersByRole(int roleId) {

        Query q = em.createQuery(
                "SELECT u FROM Users u WHERE u.rid.rid = :rid"
        );

        q.setParameter("rid", roleId);

        return q.getResultList();
    }

    // 4️⃣ Activate user
    @Override
    public void activateUser(int userId) {

        Users user = em.find(Users.class, userId);

        user.setStatus("ACTIVE");

        em.merge(user);
    }

    // 5️⃣ Deactivate user
    @Override
    public void deactivateUser(int userId) {

        Users user = em.find(Users.class, userId);

        user.setStatus("INACTIVE");

        em.merge(user);
    }

    // 6️⃣ Find user by email (Login ready)
    @Override
    public Users findUserByEmail(String email) {

        Query q = em.createNamedQuery("Users.findByEmail");
        q.setParameter("email", email);

        List<Users> list = q.getResultList();

        return list.isEmpty() ? null : list.get(0);
    }
    
//    @Override
//    public Users login(String email, String password) {
//
//
//    Query q = em.createNamedQuery("Users.findByEmail");
//    q.setParameter("email", email);
//
//    List<Users> list = q.getResultList();
//
//    if (list.isEmpty()) {
//        return null;
//    }
//
//    Users user = list.get(0);
//
//    // Check password using BCrypt
//    if (PasswordUtil.checkPassword(password, user.getPassword())) {
//
//        // Check status
//        if (!user.getStatus().equals("ACTIVE")) {
//            return null;
//        }
//
//        return user;
//    }
//
//    return null;
//    }  
    
    @Override
public Users login(String email, String password) {

    Query q = em.createNamedQuery("Users.findByEmail");
    q.setParameter("email", email);

    List<Users> list = q.getResultList();

    if (list.isEmpty()) {
        return null;
    }

    Users user = list.get(0);

    try {
        // ✅ Try BCrypt check
        if (PasswordUtil.checkPassword(password, user.getPassword())) {

            if (!user.getStatus().equals("ACTIVE")) {
                return null;
            }

            return user;
        }
    } catch (Exception e) {

        // 🔥 FALLBACK (for old plain passwords)
        if (user.getPassword().equals(password)) {

            // Optional: upgrade to hashed password
            user.setPassword(PasswordUtil.hashPassword(password));
            em.merge(user);

            return user;
        }
    }

    return null;
}
}