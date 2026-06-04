package com.fms.service;

import jakarta.ejb.Local;
import java.util.List;
import com.fms.entity.Payments;

@Local
public interface PaymentServiceLocal {

    public void createPayment(Payments payment);

    public Payments getPaymentById(int paymentId);

    public List<Payments> getAllPayments();

}