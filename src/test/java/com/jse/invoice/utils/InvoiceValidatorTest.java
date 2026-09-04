package com.jse.invoice.utils;

import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jse.invoice.dto.InvoiceDto;
import com.jse.invoice.dto.TransactionDto;
import com.jse.invoice.entity.Invoice;
import com.jse.invoice.entity.Transaction;

@ExtendWith(MockitoExtension.class)
public class InvoiceValidatorTest {

    @InjectMocks
    private InvoiceValidator invoiceValidator;

    @Test
    void shouldVerifyNoOfTransactions() {
        InvoiceDto invoiceDto = new InvoiceDto(
            "00001", 
            new BigDecimal(10.0), 
            new BigDecimal(10.0), 
            new BigDecimal(10.0), 
            LocalDateTime.now(), 
            LocalDateTime.now(), 
            4,);

        TransactionDto transactionDto = new TransactionDto(null, null, null, null, null, null, null);

        Optional<String> validationResult = invoiceValidator.verifyInvoice(invoiceDto);
    }
}
