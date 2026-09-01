package com.jse.invoice.controller;

import com.jse.invoice.dto.InvoiceDto;
import com.jse.invoice.entity.Invoice;
import com.jse.invoice.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public List<Invoice> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.getInvoice(id));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<String> getInvoiceStatus(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.getInvoiceStatus(id));
    }

    @PostMapping
    public ResponseEntity<String> createInvoice(@RequestBody InvoiceDto invoiceDto) {
        Invoice invoice = invoiceService.saveInvoice(invoiceDto);

        // Handle your business logic here (e.g., saving to a database)
        String message = "Invoice " + invoiceDto.invoiceNumber() + " created successfully - " + invoice.toString() ;

        // Return 201 Created status code along with a response message
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }
}
