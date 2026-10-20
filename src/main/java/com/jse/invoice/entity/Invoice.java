package com.jse.invoice.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jse.invoice.exception.InvalidTransactionException;

@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @Column(name = "invoice_id")
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "invoice_id_seq")
    @SequenceGenerator(
        name = "invoice_id_seq", 
        sequenceName = "invoice_id_seq", 
        initialValue = 32000, allocationSize = 1)
    private long id;
    
    @Column(name = "invoice_number")
    private String invoiceNumber;
    
    @Column(name = "gross_amount")
    private BigDecimal grossAmount;
    
    @Column(name = "gst_amount")
    private BigDecimal gstAmount;
    
    @Column(name = "net_amount")
    private BigDecimal netAmount;
    
    @Column(name = "receipt_date")
    private LocalDateTime receiptDate;
    
    @Column(name = "payment_due_date")
    private LocalDateTime paymentDueDate;

    @Column(name = "total_no_trxn")
    private long  totalNoTransactions;

    @Column(name = "is_valid")
    private boolean isValid;

    @Column(name = "reason")
    private String reason;

    // mappedBy points to the variable name in the Employee class
    // orphanRemoval ensures children are deleted if removed from this list
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Transaction> transactions = new ArrayList<>();

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public BigDecimal getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }

    public BigDecimal getGstAmount() {
        return gstAmount;
    }

    public void setGstAmount(BigDecimal gstAmount) {
        this.gstAmount = gstAmount;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public LocalDateTime getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDateTime receiptDate) {
        this.receiptDate = receiptDate;
    }

    public LocalDateTime getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(LocalDateTime paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public long getTotalNoTransactions() {
        return totalNoTransactions;
    }

    public void setTotalNoTransactions(long totalNoTransactions) {
        this.totalNoTransactions = totalNoTransactions;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    // Helper methods to keep both sides of the relationship synchronized
    public void addTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new InvalidTransactionException("Transaction cannot be null");
        }

        transactions.add(transaction);
        transaction.setInvoice(this);
    }

    public void removeTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new InvalidTransactionException("Transaction cannot be null");
        }

        if (transactions.remove(transaction)) {
            transaction.setInvoice(null);
        }
    }

    @Override
    public String toString() {
        return "Invoice [id=" + id + ", invoiceNumber=" + invoiceNumber + ", grossAmount=" + grossAmount
                + ", gstAmount=" + gstAmount + ", netAmount=" + netAmount + ", receiptDate=" + receiptDate
                + ", paymentDueDate=" + paymentDueDate + ", totalNoTransactions=" + totalNoTransactions + ", isValid="
                + isValid + ", reason=" + reason + ", transactions=" + transactions + "]";
    }

    
}
