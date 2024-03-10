package com.application.vehicleservicemanagement.controller;

import com.application.vehicleservicemanagement.service.implementation.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayInputStream;

@Controller
@RequestMapping("/pdf")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/createInvoice")
    public ResponseEntity<InputStreamResource> createInvoice() {
        ByteArrayInputStream invoice = invoiceService.createInvoice();

        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("Content-Disposition","inline;file=invoice.pdf");
        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(invoice));
    }
}
