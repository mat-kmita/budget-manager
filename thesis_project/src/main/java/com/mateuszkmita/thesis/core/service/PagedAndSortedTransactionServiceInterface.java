package com.mateuszkmita.thesis.core.service;

import com.mateuszkmita.thesis.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface PagedAndSortedTransactionServiceInterface extends TransactionServiceInterface {
    Page<Transaction> findTransactionsByAccountId(int accountId, Transaction.Fields sortField,
                                                     Sort.Direction direction, int page, int length);
}
