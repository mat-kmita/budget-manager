package com.mateuszkmita.thesis.core.service;

import com.mateuszkmita.thesis.model.BudgetCategory;

import java.util.Optional;

public interface BudgetCategoryServiceInterface {
    Optional<BudgetCategory> findBudgetCategory(int id);
    BudgetCategory updateBudgetCategoryEntity(BudgetCategory updatedEntity);
}
