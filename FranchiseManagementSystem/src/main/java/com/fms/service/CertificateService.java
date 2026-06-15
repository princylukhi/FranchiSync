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

import com.lowagie.text.Rectangle;
import java.awt.Color;


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
        
// OUTER BORDER

Rectangle outerBorder =
        new Rectangle(
                20,
                20,
                575,
                822);

outerBorder.setBorder(
        Rectangle.BOX);

outerBorder.setBorderWidth(5);

outerBorder.setBorderColor(
        new Color(
                16,
                185,
                129));

document.add(outerBorder);


// INNER BORDER

Rectangle innerBorder =
        new Rectangle(
                35,
                35,
                560,
                807);

innerBorder.setBorder(
        Rectangle.BOX);

innerBorder.setBorderWidth(2);

innerBorder.setBorderColor(
        new Color(
                52,
                211,
                153));

document.add(innerBorder);
        
        // LOGO

//Image logo =
//        Image.getInstance(logoPath);
//
//logo.scaleToFit(120, 120);
//
//logo.setAlignment(
//        Element.ALIGN_CENTER);
//
//document.add(logo);
//
//document.add(
//        new Paragraph(" ")
//);

document.add(
        new Paragraph("\n\n"));

try {

    Image logo =
            Image.getInstance(
                    logoPath);

    logo.scaleToFit(
            120,
            120);

    logo.setAlignment(
            Element.ALIGN_CENTER);

    document.add(logo);

} catch(Exception e) {

    e.printStackTrace();
}
// FONTS

Font titleFont =
        FontFactory.getFont(
                FontFactory.TIMES_BOLD,
                28);

Font ownerFont =
        FontFactory.getFont(
                FontFactory.TIMES_BOLDITALIC,
                30);

Font companyFont =
        FontFactory.getFont(
                FontFactory.TIMES_BOLD,
                26);

Font normalFont =
        FontFactory.getFont(
                FontFactory.HELVETICA,
                13);

Font sealFont =
        FontFactory.getFont(
                FontFactory.HELVETICA_BOLD,
                15);

// TITLE

document.add(
        new Paragraph("\n"));

Paragraph title =
        new Paragraph(
                franchise.getCid()
                         .getCompanyName()
                         .toUpperCase()
                + " FRANCHISE\nCERTIFICATE",
                titleFont);

title.setAlignment(
        Element.ALIGN_CENTER);

document.add(title);


document.add(
        new Paragraph(" "));

// SUBTITLE

document.add(
        new Paragraph("\n"));

Paragraph subtitle =
        new Paragraph(
        "This certificate is proudly presented to",
        normalFont);

subtitle.setAlignment(
        Element.ALIGN_CENTER);

document.add(subtitle);


// OWNER NAME

Paragraph owner =
        new Paragraph(
        franchise.getOwnerUserId()
                 .getName(),
        ownerFont);

owner.setAlignment(
        Element.ALIGN_CENTER);

owner.setSpacingAfter(0);

document.add(owner);

Paragraph line =
        new Paragraph(
        "__________________________________");

line.setAlignment(
        Element.ALIGN_CENTER);

line.setSpacingBefore(-10);

document.add(line);

// TEXT

document.add(
        new Paragraph(" "));

Paragraph text =
        new Paragraph(
        "For becoming an officially licensed franchise partner of",
        normalFont);

text.setAlignment(
        Element.ALIGN_CENTER);

document.add(text);

document.add(
        new Paragraph(" "));

// COMPANY NAME

Paragraph company =
        new Paragraph(
        franchise.getCid()
                 .getCompanyName(),
        companyFont);

company.setAlignment(
        Element.ALIGN_CENTER);

document.add(company);

// CERTIFICATE NUMBER
document.add(
        new Paragraph("\n"));

Paragraph certNo =
        new Paragraph(
        "Certificate No : "
        + certificateNumber,
        normalFont);

certNo.setAlignment(
        Element.ALIGN_CENTER);

document.add(certNo);

Paragraph issue =
        new Paragraph(
        "Issue Date : "
        + new SimpleDateFormat(
                "dd MMM yyyy")
        .format(
                franchise.getCreatedDate()),
        normalFont);

issue.setAlignment(
        Element.ALIGN_CENTER);

document.add(issue);

// VERIFICATION TEXT

document.add(
        new Paragraph("\n"));

Paragraph verify =
        new Paragraph(
        "This certificate confirms that the holder "
      + "has fulfilled all franchise requirements "
      + "and is hereby recognized as an officially "
      + "authorized franchise partner of "
      + franchise.getCid()
                 .getCompanyName()
      + ".",
        normalFont);

verify.setAlignment(
        Element.ALIGN_CENTER);

document.add(verify);

// SIGNATURE 
document.add( new Paragraph("\n\n")); 
Font signFont = FontFactory.getFont( FontFactory.TIMES_ITALIC, 24); 
Paragraph companySignature = new Paragraph( franchise.getCid() .getContactPerson(), signFont); 
companySignature.setAlignment( Element.ALIGN_CENTER); 
companySignature.setSpacingAfter(0);
document.add(companySignature); 
Paragraph signLine = new Paragraph( "____________________"); 
signLine.setAlignment( Element.ALIGN_CENTER);
signLine.setSpacingBefore(-10);
document.add(signLine); 
Paragraph authority = new Paragraph( "Authorized Signatory", normalFont); 
authority.setAlignment( Element.ALIGN_CENTER); 
document.add(authority);

        document.close();

        return fullPath;

    } catch(Exception e) {

        e.printStackTrace();

        return null;
    }
}
}