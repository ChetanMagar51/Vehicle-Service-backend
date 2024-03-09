package com.application.vehicleservicemanagement.service.implementation;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class InvoiceService {
    private Logger logger=LoggerFactory.getLogger(InvoiceService.class);

    public ByteArrayInputStream createInvoice(){
        logger.info("Create PDF Started");

        String title="Invoice";
        String content="Vehicle Service Management invoice details";

        ByteArrayOutputStream out=new ByteArrayOutputStream();
        Document document=new Document();

        PdfWriter.getInstance(document,out);

        document.open();

        Font titleFont= FontFactory.getFont(FontFactory.HELVETICA_BOLD,35, Color.BLUE);
        Paragraph titlePara=new Paragraph(title,titleFont);
        titlePara.setAlignment(Element.ALIGN_CENTER);

        document.add(titlePara);

        Font paraFont=FontFactory.getFont(FontFactory.HELVETICA,20, Color.BLACK);
        Paragraph paragraph=new Paragraph(content);
        document.add(paragraph);

        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}
