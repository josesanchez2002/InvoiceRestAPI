package com.jse.invoice.service;

import com.jse.invoice.dto.InvoiceDto;
import com.jse.invoice.entity.Invoice;

import com.jse.invoice.repository.InvoiceRepository;
import com.jse.invoice.utils.InvoiceGenerator;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    private final InvoiceGenerator invoiceGenerator;
    
    public InvoiceService(InvoiceRepository invoiceRepository, InvoiceGenerator invoiceGenerator) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceGenerator = invoiceGenerator;
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Invoice getInvoice(long id) {
        return invoiceRepository.findById(id).orElse(null);
    }

    public String getInvoiceStatus(long id) {
        Invoice invoice = getInvoice(id);

        if (invoice != null){
            return "Valid: " + invoice.isValid() + " - Reason: " + invoice.getReason();
        }

        return null;
    }

    @Transactional
    public Invoice saveInvoice(InvoiceDto invoiceDto) {
        Invoice invoice = invoiceGenerator.generateInvoice(invoiceDto);

        return invoiceRepository.save(invoice);
    }
}
