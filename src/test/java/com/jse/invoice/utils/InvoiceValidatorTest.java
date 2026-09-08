package com.jse.invoice.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jse.invoice.dto.InvoiceDto;
import com.jse.invoice.dto.TransactionDto;

@ExtendWith(MockitoExtension.class)
public class InvoiceValidatorTest {

    @InjectMocks
    private InvoiceValidator invoiceValidator;

    @Test
    void shouldVerifyNoOfTransactions() {
        TransactionDto transactionDto = new TransactionDto(LocalDateTime.now(), 
        LocalDateTime.now(), 
        "00001", LocalDateTime.now(), LocalDateTime.now(), new BigDecimal(10), new BigDecimal(10));

        List<TransactionDto> transactionDtoList = new ArrayList<>();
        transactionDtoList.add(transactionDto);

        InvoiceDto invoiceDto = new InvoiceDto(
            "00001", 
            new BigDecimal(10.0), 
            new BigDecimal(10.0), 
            new BigDecimal(10.0), 
            LocalDateTime.now(), 
            LocalDateTime.now(), 
            Long.valueOf(4), 
            transactionDtoList);


        Optional<String> validationResult = invoiceValidator.verifyInvoice(invoiceDto);

        assertEquals("List of transactions does not match the number of transactions in the invoice", validationResult.get());


    }
}
