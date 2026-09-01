package com.jse.invoice.repository;

import com.jse.invoice.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.invoice.id = ?1")
    List<Transaction> findTransactionsByInvoiceId(long invoiceId);
}
