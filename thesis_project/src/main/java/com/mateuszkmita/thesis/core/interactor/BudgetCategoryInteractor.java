package com.mateuszkmita.thesis.core.interactor;

import com.mateuszkmita.thesis.core.exception.ResourceNotFoundException;
import com.mateuszkmita.thesis.core.service.BudgetCategoryServiceInterface;
import com.mateuszkmita.thesis.external.repository.BudgetCategoryRepositoryInterface;
import com.mateuszkmita.thesis.external.repository.BudgetRepositoryInterface;
import com.mateuszkmita.thesis.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetCategoryInteractor implements BudgetCategoryServiceInterface {

    private final BudgetCategoryRepositoryInterface budgetCategoryRepository;

    private final BudgetRepositoryInterface budgetRepository;

    @Override
    public Optional<BudgetCategory> findBudgetCategory(int id) {
        return budgetCategoryRepository.findById(id);
    }

    @Override
    public Optional<BudgetCategory> findBudgetCategoryByCategoryAndDate(Category category, LocalDate date) {
        return budgetCategoryRepository.findByCategoryAndDate(category.getId(), date.getMonthValue(), date.getYear());
    }

    @Override
    public BudgetCategory updateBudgetCategoryAmountById(int budgetId, int categoryId, int amount)
            throws ResourceNotFoundException {

        Budget existing = this.budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("budget", budgetId));

        Optional<BudgetCategory> optionalBudgetCategory = existing.getBudgetCategories().stream().filter(budgetCategory -> budgetCategory.getCategory().getId() == categoryId).findFirst();

        if (optionalBudgetCategory.isEmpty()) {
            throw new ResourceNotFoundException("budget category", categoryId);
        }

        BudgetCategory budgetCategory = optionalBudgetCategory.get();
        int oldAvailable = budgetCategory.available();

        int change = amount - budgetCategory.getAmount();
        budgetCategory.setAmount(amount);

        int newAvailable = budgetCategory.available();

        if (newAvailable < 0) {
            int overspentChange = oldAvailable < 0 ? oldAvailable - newAvailable : newAvailable;
            existing.setOverspentCategoriesAmountSum(existing.getOverspentCategoriesAmountSum() - overspentChange);
        }

        existing.updateBudgeted(change);

        budgetRepository.save(existing);
        return budgetCategory;
    }

    public BudgetCategory saveBudgetCategory(BudgetCategory budgetCategory) {
        if (budgetCategory.getId() != null) {
            throw new IllegalArgumentException("New budget category must not have an ID!");
        }

        budgetCategoryRepository.save(budgetCategory);
        return budgetCategory;
    }

    public BudgetCategory addTransaction(BudgetCategory budgetCategory, Transaction transaction) {
        if (budgetCategory.getId() == null) {
            throw new IllegalArgumentException("Transaction ID must not be null! Transaction must already exist!");
        }

        // Register new expense in BudgetCategory object
        budgetCategory.addTransaction(transaction);

        return budgetCategory;
    }
}
