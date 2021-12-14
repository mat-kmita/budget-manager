package com.mateuszkmita.thesis.external.repository;

import com.mateuszkmita.thesis.model.Category;
import com.mateuszkmita.thesis.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TransactionsRepositoryInterface extends PagingAndSortingRepository<Transaction, Integer> {
    Page<Transaction> findTransactionByAccount_Id(int accountId, Pageable pageable);

    @Query(value = "SELECT sum(t.amount) FROM transactions t JOIN categories c on t.category_id = c.id WHERE c.id = 1 AND EXTRACT(MONTH FROM t.date) = :month AND EXTRACT(YEAR FROM date) = :year", nativeQuery = true)
    Integer findIncome(int month, int year);

    @Query(value = "SELECT COALESCE(SUM(t.amount), 0) FROM transactions t JOIN categories c on t.category_id = c.id WHERE c.id = :categoryId AND EXTRACT(MONTH FROM t.date) = :month AND EXTRACT(YEAR FROM date) = :year", nativeQuery = true)
    Integer findAmount(int categoryId, int month, int year);
}
