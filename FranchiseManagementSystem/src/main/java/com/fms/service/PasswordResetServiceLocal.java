package com.fms.service;

import com.fms.entity.PasswordResetTokens;
import com.fms.entity.Users;
import jakarta.ejb.Local;

@Local
public interface PasswordResetServiceLocal {

    public String generateToken();

    public void createResetToken(Users user);

    public PasswordResetTokens findByToken(String token);

    public boolean isValidToken(String token);

    public void markTokenUsed(String token);
    
    public PasswordResetTokens findLatestByUser(int uid);
}