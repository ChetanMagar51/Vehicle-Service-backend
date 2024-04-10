package com.application.vehicleservicemanagement.controller;

import com.application.vehicleservicemanagement.service.implementation.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@Controller
@RequestMapping("/invoice")
@RequiredArgsConstructor
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/generate")
    public ResponseEntity<InputStreamResource> createInvoice(@RequestParam(value = "vehicleId") Long id) {
        ByteArrayInputStream invoice = invoiceService.createInvoice(id);

        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("Content-Disposition","inline;file=invoice.pdf");
        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(invoice));
    }
}
