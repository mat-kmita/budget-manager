package com.mateuszkmita.thesis.core.service;

import com.mateuszkmita.thesis.model.Transaction;

import java.util.Optional;

public interface TransactionServiceInterface {
    Iterable<Transaction> findAllTransactions();
    Optional<Transaction> findTransactionById(int id);
    Transaction saveTransactionEntity(Transaction transaction);
    Transaction updateTransactionEntity(Transaction oldTransactio, Transaction updatedTransaction);
    void deleteTransaction(Transaction transaction);
}
