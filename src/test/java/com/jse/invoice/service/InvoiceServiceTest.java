package com.jse.invoice.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jse.invoice.dto.InvoiceDto;
import com.jse.invoice.entity.Invoice;
import com.jse.invoice.exception.InvoiceNotFoundException;
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
        List<Invoice> invoiceList = List.of(new Invoice(), new Invoice());

        when(invoiceRepository.findAll()).thenReturn(invoiceList);

        List<Invoice> returnedInvoices = invoiceService.getAllInvoices();

        verify(invoiceRepository, times(1)).findAll();

        assertThat(returnedInvoices, is(sameInstance(invoiceList)));
    }

    @Test
    void shouldGetValidInvoice() {
        Invoice invoice = new Invoice();

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

        Invoice returnedInvoice = invoiceService.getInvoice(1L);

        verify(invoiceRepository, times(1)).findById(1L);

        assertThat(returnedInvoice, is(sameInstance(invoice)));
    }

    @Test
    void shouldThrowInvoiceNotFoundExceptionWhenInvoiceDoesNotExist() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());

        InvoiceNotFoundException exception = assertThrows(InvoiceNotFoundException.class,
            () -> invoiceService.getInvoice(1L));

        verify(invoiceRepository, times(1)).findById(1L);

        assertThat(exception.getMessage(), is("Invoice with id 1 not found"));
    }

    @Test
    void shouldReturnStringInvoiceStatus() {
        Invoice invoice = new Invoice();
        invoice.setValid(false);
        invoice.setReason("reason 01");

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

        String reason = invoiceService.getInvoiceStatus(1L);

        verify(invoiceRepository, times(1)).findById(1L);

        assertThat(reason, is("Valid: false - Reason: reason 01"));
    }

    @Test
    void shouldThrowInvoiceNotFoundExceptionWhenGettingStatusOfMissingInvoice() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());

        InvoiceNotFoundException exception = assertThrows(InvoiceNotFoundException.class,
            () -> invoiceService.getInvoiceStatus(1L));

        verify(invoiceRepository, times(1)).findById(1L);

        assertThat(exception.getMessage(), is("Invoice with id 1 not found"));
    }

    @Test
    void shouldCreateNewInvoice() {
        Invoice invoice = new Invoice();
        InvoiceDto invoiceDto = new InvoiceDto(
            "00001",
            new BigDecimal("110.00"),
            new BigDecimal("10.00"),
            new BigDecimal("100.00"),
            LocalDateTime.of(2026, 2, 1, 9, 0),
            LocalDateTime.of(2026, 3, 1, 9, 0),
            Long.valueOf(0),
            List.of());

        when(invoiceGenerator.generateInvoice(invoiceDto)).thenReturn(invoice);
        when(invoiceRepository.save(invoice)).thenReturn(invoice);

        Invoice savedInvoice = invoiceService.saveInvoice(invoiceDto);

        verify(invoiceRepository, times(1)).save(invoice);

        assertThat(savedInvoice, is(sameInstance(invoice)));
    }
}
