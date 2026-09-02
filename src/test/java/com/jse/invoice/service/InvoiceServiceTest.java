package com.jse.invoice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jse.invoice.dto.InvoiceDto;
import com.jse.invoice.entity.Invoice;
import com.jse.invoice.repository.InvoiceRepository;
import com.jse.invoice.utils.InvoiceGenerator;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private InvoiceGenerator invoiceGenerator;

    @InjectMocks
    private InvoiceService invoiceService;

    @Test
    void shouldReturnAllInvoices() {
        invoiceService.getAllInvoices();
        verify(invoiceRepository, times(1)).findAll();
    }

    @Test
    void shouldGetValidInvoice() {
        Invoice invoice = new Invoice();

        when(invoiceRepository.findById(1l)).thenReturn(Optional.of(invoice));

        Invoice retuInvoice = invoiceService.getInvoice(1L);

        verify(invoiceRepository, times(1)).findById(1L);

        assertNotNull(retuInvoice);
    }

    @Test
    void shouldGetNullInvoice() {
        when(invoiceRepository.findById(1l)).thenReturn(Optional.empty());

        Invoice retuInvoice = invoiceService.getInvoice(1L);

        verify(invoiceRepository, times(1)).findById(1L);

        assertNull(retuInvoice);
    }

    @Test
    void shouldReturnStringInvoiceStatus() {
        Invoice invoice = new Invoice();
        invoice.setValid(false);
        invoice.setReason("reason 01");

        when(invoiceRepository.findById(1l)).thenReturn(Optional.of(invoice));

        String reason = invoiceService.getInvoiceStatus(1L);

        verify(invoiceRepository, times(1)).findById(1L);

        assertEquals("Valid: false - Reason: reason 01", reason);
    }

    @Test
    void shouldReturnNullInvoiceStatus() {
        when(invoiceRepository.findById(1l)).thenReturn(Optional.empty());

        String reason = invoiceService.getInvoiceStatus(1L);

        verify(invoiceRepository, times(1)).findById(1L);

        assertNull(reason);
    }

    @Test
    void shouldCreateNewInvoice() {
        Invoice invoice = mock(Invoice.class);
        InvoiceDto invoiceDto = mock(InvoiceDto.class);

        when(invoiceGenerator.generateInvoice(invoiceDto)).thenReturn(invoice);

        invoiceService.saveInvoice(invoiceDto);

        verify(invoiceRepository, times(1)).save(invoice);
    }
}
