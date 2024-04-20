package com.application.vehicleservicemanagement.service.implementation;

import com.application.vehicleservicemanagement.entity.Item;
import com.application.vehicleservicemanagement.entity.Owner;
import com.application.vehicleservicemanagement.entity.ServiceRecord;
import com.application.vehicleservicemanagement.entity.Vehicle;
import com.application.vehicleservicemanagement.exception.ResourceNotFoundException;
import com.application.vehicleservicemanagement.repository.VehicleRepository;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final VehicleRepository vehicleRepository;

    private final Logger logger = LoggerFactory.getLogger(InvoiceService.class);

    public ByteArrayInputStream createInvoice(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(() -> new ResourceNotFoundException("Vehicle", "id", vehicleId.toString()));
        ServiceRecord serviceRecord = vehicle.getServiceRecord();
        Owner owner = vehicle.getOwner();
        Map<Item, Integer> itemQuantityMap = serviceRecord.getItemQuantityMap();

        logger.info("Create PDF Started");

        ByteArrayOutputStream out=new ByteArrayOutputStream();
        Document document=new Document();
        document.setPageSize(PageSize.A4);

        try {
            PdfWriter.getInstance(document, out);

            document.open();

            float col = 300f;
            float[] colWidth = {col, col, col};
            PdfPTable table = new PdfPTable(colWidth);
            table.setWidthPercentage(100);

            try {
                Image image = Image.getInstance("src/main/resources/static/logo.png");
                image.scaleAbsolute(100,70);
                PdfPCell imageCell = new PdfPCell(image);
                imageCell.setBackgroundColor(new Color(54, 191, 255));
                imageCell.setBorder(Rectangle.NO_BORDER);
                table.addCell(imageCell);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            PdfPCell cell1 = new PdfPCell(new Phrase("INVOICE", new Font(Font.TIMES_ROMAN, 25, Font.BOLD, Color.WHITE)));
            cell1.setBackgroundColor(new Color(54, 191, 255));
            cell1.setVerticalAlignment(Element.ALIGN_CENTER);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setPadding(20);
            table.addCell(cell1);

            PdfPCell cell2 = new PdfPCell(new Phrase("Prime Automobiles\nPune, Maharashtra", new Font(Font.TIMES_ROMAN, 10, Font.NORMAL, Color.WHITE)));
            cell2.setBackgroundColor(new Color(54, 191, 255));
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell2.setPadding(15);
            table.addCell(cell2);

            for (PdfPCell cell : table.getRow(0).getCells()) {
                cell.setBorder(Rectangle.NO_BORDER);
            }

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.BLACK); // RGB Color
            Paragraph titlePara = new Paragraph(" Vehicle and Customer Details", titleFont);

            float[] col2Width = {100, 300, 100, 150};
            PdfPTable tableVehicle = new PdfPTable(col2Width);
            tableVehicle.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            tableVehicle.addCell(new Phrase("Owner", new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.BLACK)));
            tableVehicle.addCell(owner.getFirstName() + " " + owner.getLastName());

            tableVehicle.addCell(new Phrase("Invoice No", new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.BLACK)));
            tableVehicle.addCell(serviceRecord.getId().toString());

            tableVehicle.addCell(new Phrase("Vehicle No", new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.BLACK)));
            tableVehicle.addCell(vehicle.getVehicleNumber());

            tableVehicle.addCell(new Phrase("Date", new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.BLACK)));
            tableVehicle.addCell(serviceRecord.getDate().format(DateTimeFormatter.ISO_DATE));

            Paragraph tablePara = new Paragraph(" Serviced Items List", titleFont);

            float[] serviceCol = {100, 300, 150, 250};
            PdfPTable serviceTable = new PdfPTable(serviceCol);
            serviceTable.getDefaultCell().setPadding(5);
            serviceTable.getDefaultCell().setBackgroundColor(new Color(206, 236, 236));

            serviceTable.addCell(new Phrase("Sr No", new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.BLACK)));
            serviceTable.addCell(new Phrase("Serviced Item", new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.BLACK)));
            serviceTable.addCell(new Phrase("Quantity", new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.BLACK)));
            serviceTable.addCell(new Phrase("Amount", new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.BLACK)));

            int srno = 1;
            for (Item item: itemQuantityMap.keySet()) {
                serviceTable.addCell(Integer.toString(srno));
                serviceTable.addCell(item.getName());
                serviceTable.addCell(itemQuantityMap.get(item).toString());
                serviceTable.addCell(itemQuantityMap.get(item).toString() + " * " + item.getPrice().toString());
                srno ++;
            }

            float[] amountCol = {550, 250};
            PdfPTable amountTable = new PdfPTable(amountCol);
            amountTable.getDefaultCell().setPadding(5);
            amountTable.addCell("Amount Total");
            amountTable.addCell(serviceRecord.getAmount().toString());

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