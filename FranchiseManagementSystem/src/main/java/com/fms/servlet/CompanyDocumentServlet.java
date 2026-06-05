package com.fms.servlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@WebServlet("/company-document/*")
public class CompanyDocumentServlet
extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String fileName =
                request.getPathInfo()
                .substring(1);

        String filePath =
                System.getProperty(
                        "com.sun.aas.instanceRoot")
                + File.separator
                + "company-documents"
                + File.separator
                + fileName;

        File file =
                new File(filePath);

        if(!file.exists()) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND);

            return;
        }

        if(fileName.endsWith(".pdf")) {

    response.setContentType(
        "application/pdf");

} else {

    response.setContentType(
        Files.probeContentType(
            file.toPath()));
}

Files.copy(
    file.toPath(),
    response.getOutputStream());

response.flushBuffer();
    }
}