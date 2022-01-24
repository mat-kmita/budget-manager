package com.mateuszkmita.thesis.external.repository;

import com.mateuszkmita.thesis.external.repository.dto.ExpensesAmountSumWithCategory;
import com.mateuszkmita.thesis.external.repository.dto.TransactionAmountSumByDateDto;
import com.mateuszkmita.thesis.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public interface TransactionsRepositoryInterface extends PagingAndSortingRepository<Transaction, Integer> {
    int countByCategory_Id(int categoryId);

    Page<Transaction> findTransactionByAccount_Id(int accountId, Pageable pageable);

    List<Transaction> findAllByAccount_Id(int accountId);

    @Query("""
            select new com.mateuszkmita.thesis.external.repository.dto.TransactionAmountSumByDateDto(
                t.date,
                -sum(t.amount))
            from Transaction t
            where t.amount < 0 and
                t.date between :startDate and :endDate
            group by t.date
            order by t.date desc""")
    List<TransactionAmountSumByDateDto> findOutcomesAmountSumDailyByDate(LocalDate startDate, LocalDate endDate);

    @Query("""
            select new com.mateuszkmita.thesis.external.repository.dto.TransactionAmountSumByDateDto(
                t.date,
                sum(t.amount))
            from Transaction t
            where t.amount > 0 and
                t.date between :startDate and :endDate
            group by t.date
            order by t.date desc""")
    List<TransactionAmountSumByDateDto> findIncomesAmountSumDailyByDate(LocalDate startDate, LocalDate endDate);

    @Query("""
select new com.mateuszkmita.thesis.external.repository.dto.ExpensesAmountSumWithCategory(-sum(t.amount), category)
            from Transaction t
            join t.category as category
            where t.amount < 0 and
                t.date between :startDate and :endDate
            group by category.id, category.name
            """)
    Stream<ExpensesAmountSumWithCategory> findExpensesCategorizedAmountSumByDate(LocalDate startDate, LocalDate endDate);
}