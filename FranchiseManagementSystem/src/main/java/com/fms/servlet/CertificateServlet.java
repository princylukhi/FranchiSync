package com.fms.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@WebServlet("/franchise-certificates/*")
public class CertificateServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse res)
            throws IOException {

        String fileName =
                req.getPathInfo().substring(1);

        String certificateDir =
                System.getProperty("com.sun.aas.instanceRoot")
                + File.separator
                + "franchise-certificates"
                + File.separator;

        File file =
                new File(certificateDir + fileName);

        if (file.exists()) {

            res.setContentType("application/pdf");

            Files.copy(
                    file.toPath(),
                    res.getOutputStream()
            );

        } else {

            res.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Certificate not found"
            );
        }
    }
}