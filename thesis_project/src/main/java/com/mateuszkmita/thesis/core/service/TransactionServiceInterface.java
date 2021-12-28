package com.mateuszkmita.thesis.core.service;

import com.mateuszkmita.thesis.model.Category;
import com.mateuszkmita.thesis.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

public interface TransactionServiceInterface {
    Page<Transaction> findTransactionsByAccountId(int accountId, Transaction.Fields sortField,
                                                  Sort.Direction direction, int page, int length);
    Optional<Transaction> findTransactionById(int id);
    Transaction saveTransactionEntity(Transaction transaction);
    Transaction updateTransactionEntity(Transaction oldTransaction, Transaction updatedTransaction);
    void deleteTransaction(Transaction transaction);
    int calculateIncomeByMonthAndYear(int monthValue, int year);
    Iterable<Transaction> findByCategoryAndDate(Category category, LocalDate date);
}
