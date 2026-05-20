/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package com.fms.service;

import com.fms.entity.Users;
import jakarta.ejb.Local;
import java.util.List;

/**
 *
 * @author Princy Lukhi
 */
@Local
public interface UserServiceLocal {
     // Create user (for all roles)
    public void createUser(Users user, int roleId, int companyId, Integer branchId);

    // Get users by company
    public List<Users> getUsersByCompany(int companyId);

    // Get users by role
    public List<Users> getUsersByRole(int roleId);

    // Activate / Deactivate
    public void activateUser(int userId);
    public void deactivateUser(int userId);

    // Find user (login purpose)
    public Users findUserByEmail(String email);
    
    public Users login(String email, String password);
    
    public List<Users> getAllUsers();

  public List<Users> getUsersByStatus(String status);
  
  public void updateUser(Users user);
    
}
