package com.fms.service;

import com.fms.entity.Franchises;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.ejb.Stateless;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

@Stateless
public class CertificateService
implements CertificateServiceLocal {

    @Override
    public String generateCertificate(
            Franchises franchise) {

    try {

        String folderPath =
        System.getProperty("com.sun.aas.instanceRoot")
        + File.separator
        + "franchise-certificates"
        + File.separator;
        
        
        File folder =
            new File(folderPath);

        if(!folder.exists()) {

            folder.mkdirs();
        }

        String fileName =
            "FRANCHISE_"
            + franchise.getFid()
            + ".pdf";
        
        franchise.setCertificateFile(fileName);
        
        String certificateNumber =
        "CERT-2026-"
        + String.format(
            "%04d",
            franchise.getFid()
        );

       String logoPath =
        System.getProperty("com.sun.aas.instanceRoot")
        + File.separator
        + "company-logos"
        + File.separator
        + franchise.getCid().getLogo();
        
        String fullPath =
            folderPath + fileName;

        Document document =
            new Document();

        PdfWriter.getInstance(
            document,
            new FileOutputStream(fullPath)
        );

        document.open();
        
        

        Font titleFont =
            FontFactory.getFont(
                FontFactory.HELVETICA_BOLD,
                20
            );

        Font normalFont =
            FontFactory.getFont(
                FontFactory.HELVETICA,
                12
            );
        
       
        
        Image logo =
        Image.getInstance(logoPath);

        logo.scaleToFit(120,120);

        logo.setAlignment(
                Element.ALIGN_CENTER);

        document.add(logo);

        Paragraph title =
    new Paragraph(
        "OFFICIAL FRANCHISE CERTIFICATE",
        titleFont
    );

title.setAlignment(
    Element.ALIGN_CENTER
);

document.add(title);

document.add(
    new Paragraph(" ")
);

Paragraph subtitle =
    new Paragraph(
        "This Certificate is Proudly Awarded To",
        normalFont
    );

subtitle.setAlignment(
    Element.ALIGN_CENTER
);

document.add(subtitle);

document.add(
    new Paragraph(" ")
);

Font ownerFont =
    FontFactory.getFont(
        FontFactory.HELVETICA_BOLD,
        26
    );

Paragraph owner =
    new Paragraph(
        franchise.getOwnerUserId()
                 .getName()
                 .toUpperCase(),
        ownerFont
    );

owner.setAlignment(
    Element.ALIGN_CENTER
);

document.add(owner);

document.add(
    new Paragraph(" ")
);

Paragraph text =
    new Paragraph(
        "For being an officially authorized franchise owner of",
        normalFont
    );

text.setAlignment(
    Element.ALIGN_CENTER
);

document.add(text);

document.add(
    new Paragraph(" ")
);

Font companyFont =
    FontFactory.getFont(
        FontFactory.HELVETICA_BOLD,
        22
    );

Paragraph company =
    new Paragraph(
        franchise.getCid()
                 .getCompanyName()
                 .toUpperCase(),
        companyFont
    );

company.setAlignment(
    Element.ALIGN_CENTER
);

document.add(company);

document.add(
    new Paragraph(" ")
);

document.add(
    new Paragraph(" ")
);

Paragraph certNo =
    new Paragraph(
        "Certificate No : "
        + certificateNumber,
        normalFont
    );

certNo.setAlignment(
    Element.ALIGN_CENTER
);

document.add(certNo);

Paragraph fid =
    new Paragraph(
        "Franchise ID : "
        + franchise.getFid(),
        normalFont
    );

fid.setAlignment(
    Element.ALIGN_CENTER
);

document.add(fid);

Paragraph date =
    new Paragraph(
        "Issue Date : "
        + new SimpleDateFormat(
            "dd MMM yyyy"
        ).format(
            franchise.getCreatedDate()
        ),
        normalFont
    );

date.setAlignment(
    Element.ALIGN_CENTER
);

document.add(date);
        
    document.add(
    new Paragraph("\n\n\n")
);

Paragraph sign =
    new Paragraph(
        "____________________________",
        normalFont
    );

sign.setAlignment(
    Element.ALIGN_RIGHT
);

document.add(sign);

Paragraph auth =
    new Paragraph(
        "Authorized Signatory",
        normalFont
    );

auth.setAlignment(
    Element.ALIGN_RIGHT
);

document.add(auth);

Paragraph companyAuth =
    new Paragraph(
        franchise.getCid()
                 .getCompanyName(),
        normalFont
    );

companyAuth.setAlignment(
    Element.ALIGN_RIGHT
);

document.add(companyAuth);
        

        document.close();

        return fullPath;

    } catch(Exception e) {

        e.printStackTrace();

        return null;
    }
}
}