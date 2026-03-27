/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package com.fms.service;

import jakarta.ejb.Local;

/**
 *
 * @author Maitri Moradiya
 */
@Local
public interface EmailServiceLocal {
    
    public void sendEmail(String to, String subject, String message);
    
}
