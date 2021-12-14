package com.mateuszkmita.thesis.core.interactor;

import com.mateuszkmita.thesis.core.exception.ResourceNotFoundException;
import com.mateuszkmita.thesis.core.service.BudgetCategoryServiceInterface;
import com.mateuszkmita.thesis.core.service.TransactionServiceInterface;
import com.mateuszkmita.thesis.external.repository.BudgetCategoryRepositoryInterface;
import com.mateuszkmita.thesis.external.repository.BudgetRepositoryInterface;
import com.mateuszkmita.thesis.model.Budget;
import com.mateuszkmita.thesis.model.BudgetCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetCategoryInteractor implements BudgetCategoryServiceInterface {

    private final BudgetCategoryRepositoryInterface budgetCategoryRepository;
    private final TransactionServiceInterface transactionService;

    @Override
    public Optional<BudgetCategory> findBudgetCategory(int id) {
        return budgetCategoryRepository.findById(id);
    }

    @Override
    public BudgetCategory updateBudgetCategoryEntity(BudgetCategory updatedEntity) {
        if (updatedEntity.getId() == null) {
            throw new IllegalArgumentException("Updated budget category must have an ID!");
        }

        return budgetCategoryRepository.save(updatedEntity);
    }

    public BudgetCategory calculateBudgetCategoryAmounts(BudgetCategory budgetCategory) {
        Budget budget = budgetCategory.getBudget();

        Optional<BudgetCategory> previousBudgetCategory = budgetCategoryRepository
                .findFirstByCategory_IdAndBudget_DateBeforeOrderByBudget_DateDesc(budgetCategory.getCategory().getId(), budget.getDate());

        int spent = transactionService.calculateAmountByCategoryAndDate(budgetCategory.getCategory(), budget.getDate().getMonthValue(), budget.getDate().getYear());
        int amount = budgetCategory.getAmount();
        return new BudgetCategory(budgetCategory.getId(), budget, budgetCategory.getCategory(),
                previousBudgetCategory
                        .map(BudgetCategory::getBalance)
                        .orElse(0) + amount + spent,
                spent, amount);
    }
}
