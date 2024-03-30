package com.application.vehicleservicemanagement.service.implementation;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
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

        ByteArrayOutputStream out=new ByteArrayOutputStream();
        Document document=new Document();
        document.setPageSize(PageSize.A4);

        try {
            PdfWriter.getInstance(document, out);

            document.open();

            float col = 280f;
            float colWidth[] = {col, col};
            PdfPTable table = new PdfPTable(colWidth);
            table.setWidthPercentage(100);

            PdfPCell cell1 = new PdfPCell(new Phrase("INVOICE", new Font(Font.TIMES_ROMAN, 20, Font.BOLD, Color.WHITE)));
            cell1.setBackgroundColor(new Color(54, 191, 255));
            cell1.setVerticalAlignment(Element.ALIGN_CENTER);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setPadding(20);
            table.addCell(cell1);

            PdfPCell cell2 = new PdfPCell(new Phrase("Company name\nPune, Maharashtra", new Font(Font.TIMES_ROMAN, 10, Font.NORMAL, Color.WHITE)));
            cell2.setBackgroundColor(new Color(54, 191, 255));
            cell2.setVerticalAlignment(Element.ALIGN_CENTER);
            cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell2.setPadding(15);
            table.addCell(cell2);

            for (PdfPCell cell : table.getRow(0).getCells()) {
                cell.setBorder(Rectangle.NO_BORDER);
            }

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.BLACK); // RGB Color
            Paragraph titlePara = new Paragraph("Vehicle and Customer Details", titleFont);

            float col2Width[] = {100, 250, 100, 150};
            PdfPTable tableVehicle = new PdfPTable(col2Width);
            tableVehicle.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            tableVehicle.addCell(new Phrase("Name", new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.BLACK)));
            tableVehicle.addCell("Vaibhavi");

            tableVehicle.addCell(new Phrase("Invoice No", new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.BLACK)));
            tableVehicle.addCell("12345");

            tableVehicle.addCell(new Phrase("Vehicle No", new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.BLACK)));
            tableVehicle.addCell("MH 12 VV 2528");

            tableVehicle.addCell(new Phrase("Date", new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.BLACK)));
            tableVehicle.addCell("10/03/2024");

            Paragraph tablePara = new Paragraph("Serviced Items List", titleFont);

            float serviceCol[] = {100, 300, 150, 250};
            PdfPTable serviceTable = new PdfPTable(serviceCol);
            serviceTable.getDefaultCell().setPadding(5);
            serviceTable.getDefaultCell().setBackgroundColor(new Color(206, 236, 236));

            serviceTable.addCell(new Phrase("Sr No", new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.BLACK)));
            serviceTable.addCell(new Phrase("Serviced Item", new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.BLACK)));
            serviceTable.addCell(new Phrase("Quantity", new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.BLACK)));
            serviceTable.addCell(new Phrase("Amount", new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.BLACK)));

            serviceTable.addCell("1");
            serviceTable.addCell("Tires");
            serviceTable.addCell("2");
            serviceTable.addCell("5500");
            serviceTable.addCell("2");
            serviceTable.addCell("Sunroof");
            serviceTable.addCell("1");
            serviceTable.addCell("1000");

            float amountCol[] = {550, 250};
            PdfPTable amountTable = new PdfPTable(amountCol);
            amountTable.getDefaultCell().setPadding(5);
            amountTable.addCell("Amount Total");
            amountTable.addCell("6500");

            document.add(table);

            document.add(new Paragraph("\n"));
            document.add(titlePara);

            document.add(new Paragraph("\n"));
            document.add(tableVehicle);

            document.add(new Paragraph("\n\n"));
            document.add(tablePara);

            document.add(new Paragraph("\n"));
            document.add(serviceTable);
            document.add(amountTable);

            document.close();

        } catch (DocumentException e) {
            logger.error("Error creating PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

}