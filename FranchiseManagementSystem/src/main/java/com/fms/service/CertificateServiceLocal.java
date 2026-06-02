package com.fms.service;

import com.fms.entity.Franchises;

public interface CertificateServiceLocal {

    String generateCertificate(
            Franchises franchise);
}