package com.fms.bean;

import com.fms.entity.Franchises;
import com.fms.entity.Users;
import com.fms.service.FranchiseServiceLocal;

import jakarta.ejb.EJB;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

@Named
@ViewScoped
public class FranchiseCertificateBean
implements Serializable {

    @EJB
    private FranchiseServiceLocal franchiseService;

    public void downloadCertificate()
            throws IOException {

        HttpSession session =
                (HttpSession) FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getSession(false);

        Users user =
                (Users) session.getAttribute("user");

        Franchises franchise =
                franchiseService
                        .getFranchiseByOwner(
                                user.getUid());

        String fileName =
                franchise.getCertificateFile();

        String fullPath =
                "C:/Users/Public/Payara_Server/"
                + "glassfish/domains/domain1/"
                + "franchise-certificates/"
                + fileName;

        File file =
                new File(fullPath);

        ExternalContext ec =
                FacesContext
                .getCurrentInstance()
                .getExternalContext();

        HttpServletResponse response =
                (HttpServletResponse)
                        ec.getResponse();

        response.reset();
        response.setContentType(
                "application/pdf");

        response.setHeader(
                "Content-Disposition",
                "attachment;filename="
                + fileName);

        try(FileInputStream fis =
                    new FileInputStream(file);

            OutputStream os =
                    response.getOutputStream()) {

            byte[] buffer =
                    new byte[1024];

            int bytesRead;

            while((bytesRead =
                    fis.read(buffer))
                    != -1) {

                os.write(
                        buffer,
                        0,
                        bytesRead);
            }
        }

        FacesContext
                .getCurrentInstance()
                .responseComplete();
    }
}