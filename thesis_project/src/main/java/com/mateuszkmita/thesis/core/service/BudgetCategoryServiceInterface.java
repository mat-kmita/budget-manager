package com.mateuszkmita.thesis.core.service;

import com.mateuszkmita.thesis.core.exception.ResourceNotFoundException;
import com.mateuszkmita.thesis.model.BudgetCategory;
import com.mateuszkmita.thesis.model.Category;
import com.mateuszkmita.thesis.model.Transaction;

import java.time.LocalDate;
import java.util.Optional;

public interface BudgetCategoryServiceInterface {
    Optional<BudgetCategory> findBudgetCategoryByCategoryAndDate(Category category, LocalDate date);

    BudgetCategory updateBudgetCategoryAmountById(int budgetId, int categoryId, int amount)
            throws ResourceNotFoundException;

    BudgetCategory addTransaction(BudgetCategory budgetCategory, Transaction transaction);

    BudgetCategory removeTransaction(BudgetCategory budgetCategory, Transaction transaction);

    Iterable<BudgetCategory> createAllForCategory(Category category);
}
