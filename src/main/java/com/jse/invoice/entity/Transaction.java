package com.jse.invoice.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "transaction_id_seq")
    @SequenceGenerator(
        name = "transaction_id_seq", 
        sequenceName = "transaction_id_seq", 
        initialValue = 73000, allocationSize = 1)
    @Column(name = "trxn_id")
    private long id;

    @Column(name = "date_received")
    private LocalDateTime dateReceived;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;
    
    @Column(name = "billing_period_start")
    private LocalDateTime billingPeriodStart;

    @Column(name = "billing_period_end")
    private LocalDateTime billingPeriodEnd;

    @Column(name = "net_transaction_amount")
    private BigDecimal netTransactionAmount;

    @Column(name = "gst_amount")
    private BigDecimal gstAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false) // Foreign key column
    private Invoice invoice;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(LocalDateTime dateReceived) {
        this.dateReceived = dateReceived;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public LocalDateTime getBillingPeriodStart() {
        return billingPeriodStart;
    }

    public void setBillingPeriodStart(LocalDateTime billingPeriodStart) {
        this.billingPeriodStart = billingPeriodStart;
    }

    public LocalDateTime getBillingPeriodEnd() {
        return billingPeriodEnd;
    }

    public void setBillingPeriodEnd(LocalDateTime billingPeriodEnd) {
        this.billingPeriodEnd = billingPeriodEnd;
    }

    public BigDecimal getNetTransactionAmount() {
        return netTransactionAmount;
    }

    public void setNetTransactionAmount(BigDecimal netTransactionAmount) {
        this.netTransactionAmount = netTransactionAmount;
    }

    public BigDecimal getGstAmount() {
        return gstAmount;
    }

    public void setGstAmount(BigDecimal gstAmount) {
        this.gstAmount = gstAmount;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    @Override
    public String toString() {
        return "Transaction [id=" + id + ", dateReceived=" + dateReceived + ", transactionDate=" + transactionDate
                + ", billingPeriodStart=" + billingPeriodStart + ", billingPeriodEnd=" + billingPeriodEnd
                + ", netTransactionAmount=" + netTransactionAmount + ", gstAmount=" + gstAmount + ", invoice=" + invoice
                + ", getId()=" + getId() + ", getDateReceived()=" + getDateReceived() + ", getClass()=" + getClass()
                + ", getTransactionDate()=" + getTransactionDate() + ", getBillingPeriodStart()="
                + getBillingPeriodStart() + ", getBillingPeriodEnd()=" + getBillingPeriodEnd()
                + ", getNetTransactionAmount()=" + getNetTransactionAmount() + ", getGstAmount()=" + getGstAmount()
                + ", getInvoice()=" + getInvoice() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
                + "]";
    }

    
}
