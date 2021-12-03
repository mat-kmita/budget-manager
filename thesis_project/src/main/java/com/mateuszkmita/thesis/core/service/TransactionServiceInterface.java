package com.mateuszkmita.thesis.core.service;

import com.mateuszkmita.thesis.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public interface TransactionServiceInterface {
    Page<Transaction> findTransactionsByAccountId(int accountId, Transaction.Fields sortField,
                                                  Sort.Direction direction, int page, int length);
    Optional<Transaction> findTransactionById(int id);
    Transaction saveTransactionEntity(Transaction transaction);
    Transaction updateTransactionEntity(Transaction oldTransaction, Transaction updatedTransaction);
    void deleteTransaction(Transaction transaction);
}
