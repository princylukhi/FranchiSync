package com.fms.service;

import jakarta.ejb.Local;
import java.util.List;
import com.fms.entity.Customers;

@Local
public interface CustomerServiceLocal {

    public void addCustomer(Customers customer);

    public void updateCustomer(Customers customer);

    public Customers findById(int customerId);

    public Customers findByMobile(String mobileNo);

    public List<Customers> getAllCustomers();
    
    public Customers findByEmail(String email);
    
    public Customers saveOrUpdate(Customers customer);

}