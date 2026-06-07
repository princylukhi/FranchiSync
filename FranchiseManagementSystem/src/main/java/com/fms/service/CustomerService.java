package com.fms.service;

import com.fms.entity.Customers;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class CustomerService implements CustomerServiceLocal {

    @PersistenceContext(unitName = "FranchisePU")
    private EntityManager em;

    @Override
    public void addCustomer(Customers customer) {

        em.persist(customer);

    }

    @Override
    public void updateCustomer(Customers customer) {

        em.merge(customer);

    }

    @Override
    public Customers findById(int customerId) {

        return em.find(Customers.class, customerId);

    }

    @Override
    public Customers findByMobile(String mobileNo) {

        List<Customers> list = em.createQuery(
                "SELECT c FROM Customers c WHERE c.mobileNo = :mobileNo",
                Customers.class)
                .setParameter("mobileNo", mobileNo)
                .getResultList();

        if (list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }

    @Override
    public List<Customers> getAllCustomers() {

        return em.createNamedQuery(
                "Customers.findAll",
                Customers.class)
                .getResultList();

    }
    
    @Override
    public Customers findByEmail(String email) {

        List<Customers> list = em.createQuery(
                "SELECT c FROM Customers c WHERE c.email = :email",
                Customers.class)
                .setParameter("email", email)
                .getResultList();

        if (list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }
    
    @Override
    public Customers saveOrUpdate(Customers customer) {

        if (customer.getCustomerId() == null) {

            customer.setCreatedAt(new java.util.Date());

            em.persist(customer);

            return customer;
        }

        return em.merge(customer);
    }
    
    @Override
public Long getTotalCustomers(int bid) {

    return em.createQuery(
        "SELECT COUNT(DISTINCT s.customerId.customerId) " +
        "FROM Sales s " +
        "WHERE s.bid.bid = :bid",
        Long.class
    )
    .setParameter("bid", bid)
    .getSingleResult();
}
}