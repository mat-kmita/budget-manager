package com.mateuszkmita.thesis.external.repository;

import com.mateuszkmita.thesis.model.BudgetCategory;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface BudgetCategoryRepositoryInterface extends CrudRepository<BudgetCategory, Integer> {
    Optional<BudgetCategory> findFirstByCategory_IdAndBudget_DateBeforeOrderByBudget_DateDesc(int categoryId, LocalDate date);
}
