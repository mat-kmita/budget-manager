package com.mateuszkmita.thesis.core.service;

import com.mateuszkmita.thesis.external.repository.dto.TransactionAmountSumByDateDto;
import com.mateuszkmita.thesis.model.Category;
import com.mateuszkmita.thesis.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TransactionServiceInterface {
    Page<Transaction> findTransactionsByAccountId(int accountId, Transaction.Fields sortField,
                                                  Sort.Direction direction, int page, int length);
    Optional<Transaction> findTransactionById(int id);
    Transaction saveTransactionEntity(Transaction transaction);
    Transaction updateTransactionEntity(Transaction oldTransaction, Transaction updatedTransaction);
    void deleteTransaction(Transaction transaction);
    void deleteTransactionsByAccountId(int accountId);

    List<TransactionAmountSumByDateDto> findOutcomesAmountDailyByDate(LocalDate startDate, LocalDate endDate);

    List<TransactionAmountSumByDateDto> findIncomesAmountDailyByDate(LocalDate startDate, LocalDate endDate);

    Map<Category, Long> findExpensesCategorizedSumByDate(LocalDate startDate, LocalDate endDate);
}
