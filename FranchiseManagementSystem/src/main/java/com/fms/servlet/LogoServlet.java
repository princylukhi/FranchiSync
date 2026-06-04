package com.fms.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/company-logo/*")
public class LogoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String fileName =
                request.getPathInfo().substring(1);

        String imagePath =
                System.getProperty("com.sun.aas.instanceRoot")
                + File.separator
                + "company-logos"
                + File.separator
                + fileName;

        File file = new File(imagePath);

        // File not found
        if (!file.exists()) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND
            );

            return;
        }

        // Content Type
        String contentType =
                getServletContext()
                .getMimeType(file.getName());

        if (contentType == null) {

            contentType = "application/octet-stream";
        }

        response.setContentType(contentType);

        response.setContentLength((int) file.length());

        // Output Stream
        FileInputStream fis =
                new FileInputStream(file);

        OutputStream os =
                response.getOutputStream();

        byte[] buffer = new byte[1024];

        int bytesRead;

        while ((bytesRead = fis.read(buffer)) != -1) {

            os.write(buffer, 0, bytesRead);
        }

        fis.close();
        os.close();
    }
}