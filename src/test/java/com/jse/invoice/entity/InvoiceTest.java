package com.jse.invoice.entity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jse.invoice.exception.InvalidTransactionException;

@ExtendWith(MockitoExtension.class)
public class InvoiceTest {

    private static final LocalDateTime FIXED_DATE = LocalDateTime.of(2026, 1, 15, 10, 0);

    @InjectMocks
    private Invoice invoice;

    @Test
    void shouldHaveEmptyTransactionListByDefault() {
        assertThat(invoice.getTransactions(), is(empty()));
    }

    @Test
    void shouldReturnValuesSetThroughSetters() {
        invoice.setId(1L);
        invoice.setInvoiceNumber("A000001");
        invoice.setGrossAmount(new BigDecimal("110.00"));
        invoice.setGstAmount(new BigDecimal("10.00"));
        invoice.setNetAmount(new BigDecimal("100.00"));
        invoice.setReceiptDate(FIXED_DATE);
        invoice.setPaymentDueDate(FIXED_DATE.plusDays(30));
        invoice.setTotalNoTransactions(2L);
        invoice.setValid(false);
        invoice.setReason("Net amount does not match");

        assertThat(invoice.getId(), is(1L));
        assertThat(invoice.getInvoiceNumber(), is("A000001"));
        assertThat(invoice.getGrossAmount(), comparesEqualTo(new BigDecimal("110.00")));
        assertThat(invoice.getGstAmount(), comparesEqualTo(new BigDecimal("10.00")));
        assertThat(invoice.getNetAmount(), comparesEqualTo(new BigDecimal("100.00")));
        assertThat(invoice.getReceiptDate(), is(FIXED_DATE));
        assertThat(invoice.getPaymentDueDate(), is(FIXED_DATE.plusDays(30)));
        assertThat(invoice.getTotalNoTransactions(), is(2L));
        assertThat(invoice.isValid(), is(false));
        assertThat(invoice.getReason(), is("Net amount does not match"));
    }

    @Test
    void shouldAddTransactionAndLinkItToInvoice() {
        Transaction transaction = transaction(1L);

        invoice.addTransaction(transaction);

        assertThat(invoice.getTransactions(), contains(sameInstance(transaction)));
        assertThat(transaction.getInvoice(), is(sameInstance(invoice)));
    }

    @Test
    void shouldAddMultipleTransactionsInOrder() {
        Transaction firstTransaction = transaction(1L);
        Transaction secondTransaction = transaction(2L);

        invoice.addTransaction(firstTransaction);
        invoice.addTransaction(secondTransaction);

        assertThat(invoice.getTransactions(), contains(sameInstance(firstTransaction), sameInstance(secondTransaction)));
        assertThat(firstTransaction.getInvoice(), is(sameInstance(invoice)));
        assertThat(secondTransaction.getInvoice(), is(sameInstance(invoice)));
    }

    @Test
    void shouldRemoveTransactionAndUnlinkItFromInvoice() {
        Transaction transaction = transaction(1L);
        invoice.addTransaction(transaction);

        invoice.removeTransaction(transaction);

        assertThat(invoice.getTransactions(), is(empty()));
        assertThat(transaction.getInvoice(), is(nullValue()));
    }

    @Test
    void shouldKeepOtherTransactionsWhenRemovingOne() {
        Transaction firstTransaction = transaction(1L);
        Transaction secondTransaction = transaction(2L);
        invoice.addTransaction(firstTransaction);
        invoice.addTransaction(secondTransaction);

        invoice.removeTransaction(firstTransaction);

        assertThat(invoice.getTransactions(), contains(sameInstance(secondTransaction)));
        assertThat(secondTransaction.getInvoice(), is(sameInstance(invoice)));
    }

    @Test
    void shouldIncludeFieldsAndTransactionsInToString() {
        invoice.setId(1L);
        invoice.setInvoiceNumber("A000001");
        invoice.setValid(true);
        invoice.addTransaction(transaction(5L));

        String invoiceString = invoice.toString();

        assertThat(invoiceString, containsString("id=1"));
        assertThat(invoiceString, containsString("invoiceNumber=A000001"));
        assertThat(invoiceString, containsString("isValid=true"));
        assertThat(invoiceString, containsString("Transaction [id=5"));
    }

    @Test
    void shouldLeaveTransactionsUnchangedWhenRemovingTransactionNotInInvoice() {
        Transaction transaction = transaction(1L);
        invoice.addTransaction(transaction);

        invoice.removeTransaction(transaction(2L));

        assertThat(invoice.getTransactions(), contains(sameInstance(transaction)));
        assertThat(transaction.getInvoice(), is(sameInstance(invoice)));
    }

    @Test
    void shouldThrowInvalidTransactionExceptionWhenAddingNullTransaction() {
        InvalidTransactionException exception = assertThrows(InvalidTransactionException.class,
            () -> invoice.addTransaction(null));

        assertThat(exception.getMessage(), is("Transaction cannot be null"));
        assertThat(invoice.getTransactions(), is(empty()));
    }

    @Test
    void shouldKeepLinkToOtherInvoiceWhenRemovingTransactionNotInInvoice() {
        Invoice otherInvoice = new Invoice();
        Transaction transaction = transaction(1L);
        otherInvoice.addTransaction(transaction);

        invoice.removeTransaction(transaction);

        assertThat(transaction.getInvoice(), is(sameInstance(otherInvoice)));
        assertThat(otherInvoice.getTransactions(), contains(sameInstance(transaction)));
    }

    @Test
    void shouldThrowInvalidTransactionExceptionWhenRemovingNullTransaction() {
        InvalidTransactionException exception = assertThrows(InvalidTransactionException.class,
            () -> invoice.removeTransaction(null));

        assertThat(exception.getMessage(), is("Transaction cannot be null"));
    }

    private Transaction transaction(long id) {
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setDateReceived(FIXED_DATE);
        transaction.setTransactionDate(FIXED_DATE);
        transaction.setBillingPeriodStart(FIXED_DATE);
        transaction.setBillingPeriodEnd(FIXED_DATE.plusMonths(1));
        transaction.setNetTransactionAmount(new BigDecimal("100.00"));
        transaction.setGstAmount(new BigDecimal("10.00"));
        return transaction;
    }
}
