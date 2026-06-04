package com.fms.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@WebServlet("/product-images/*")
public class ProductImageServlet extends HttpServlet {

    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse res)
            throws IOException {

        String fileName = req.getPathInfo().substring(1);

        String uploadDir =
                System.getProperty("com.sun.aas.instanceRoot")
                + File.separator
                + "product-images"
                + File.separator;

        File file = new File(uploadDir + fileName);

        if (file.exists()) {

            String mime =
                    getServletContext().getMimeType(fileName);

            res.setContentType(
                    mime != null ? mime : "image/jpeg"
            );

            Files.copy(
                    file.toPath(),
                    res.getOutputStream()
            );

        } else {

            res.sendError(404, "Image not found");
        }
    }
}