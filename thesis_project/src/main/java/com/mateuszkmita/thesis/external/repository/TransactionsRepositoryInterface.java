package com.mateuszkmita.thesis.external.repository;

import com.mateuszkmita.thesis.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TransactionsRepositoryInterface extends PagingAndSortingRepository<Transaction, Integer> {
    Page<Transaction> findTransactionByAccount_Id(int accountId, Pageable pageable);

    List<Transaction> findAllByAccount_Id(int accountId);
}