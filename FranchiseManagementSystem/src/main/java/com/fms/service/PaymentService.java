package com.fms.service;

import com.fms.entity.Payments;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Stateless
public class PaymentService implements PaymentServiceLocal {

    @PersistenceContext(unitName = "FranchisePU")
    private EntityManager em;

    @Override
    public void createPayment(Payments payment) {

        payment.setPaymentDate(new Date());

        if (payment.getPaymentStatus() == null) {
            payment.setPaymentStatus("PAID");
        }

        em.persist(payment);
    }

    @Override
    public Payments getPaymentById(int paymentId) {

        return em.find(Payments.class, paymentId);

    }

    @Override
    public List<Payments> getAllPayments() {

        return em.createNamedQuery(
                "Payments.findAll",
                Payments.class)
                .getResultList();

    }
}