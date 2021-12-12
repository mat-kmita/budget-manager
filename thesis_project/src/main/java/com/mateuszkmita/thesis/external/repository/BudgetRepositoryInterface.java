package com.mateuszkmita.thesis.external.repository;

import com.mateuszkmita.thesis.model.Budget;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BudgetRepositoryInterface extends CrudRepository<Budget, Integer> {

    @Query(value = "SELECT * FROM budgets WHERE EXTRACT(MONTH FROM month_year) = :month AND EXTRACT(YEAR FROM month_year) = :year",
            nativeQuery = true)
    Optional<Budget> findByMonthAndYear(int month, int year);
}
