package com.mateuszkmita.thesis.core.interactor;

import com.mateuszkmita.thesis.core.exception.ResourceNotFoundException;
import com.mateuszkmita.thesis.core.service.BudgetCategoryServiceInterface;
import com.mateuszkmita.thesis.core.service.BudgetServiceInterface;
import com.mateuszkmita.thesis.external.repository.BudgetCategoryRepositoryInterface;
import com.mateuszkmita.thesis.external.repository.BudgetRepositoryInterface;
import com.mateuszkmita.thesis.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class BudgetCategoryInteractor implements BudgetCategoryServiceInterface {

    private final BudgetCategoryRepositoryInterface budgetCategoryRepository;
    private final BudgetRepositoryInterface budgetRepository;

    @Override
    public Optional<BudgetCategory> findBudgetCategoryByCategoryAndDate(Category category, LocalDate date) {
        return budgetCategoryRepository.findByCategoryAndDate(category.getId(), date.getMonthValue(), date.getYear());
    }

    @Override
    // TODO dont allow updating budget for incomes
    public BudgetCategory updateBudgetCategoryAmountById(int budgetId, int categoryId, int amount)
            throws ResourceNotFoundException {

        Budget existing = this.budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("budget", budgetId));

        Optional<BudgetCategory> optionalBudgetCategory = existing.getBudgetCategories().stream().filter(budgetCategory -> budgetCategory.getCategory().getId() == categoryId).findFirst();

        if (optionalBudgetCategory.isEmpty()) {
            throw new ResourceNotFoundException("budget category", categoryId);
        }

        BudgetCategory budgetCategory = optionalBudgetCategory.get();
        int change = amount - budgetCategory.getAmount();
        budgetCategory.setAmount(amount);
        existing.updateBudgeted(change);
        budgetRepository.save(existing);

        return budgetCategory;
    }

    public BudgetCategory addTransaction(BudgetCategory budgetCategory, Transaction transaction) {
        // Register new expense in BudgetCategory object
        budgetCategory.setSpent(budgetCategory.getSpent() + transaction.getAmount());

        return budgetCategoryRepository.save(budgetCategory);
    }

    @Override
    public BudgetCategory removeTransaction(BudgetCategory budgetCategory, Transaction transaction) {
        budgetCategory.setSpent(budgetCategory.getSpent() - transaction.getAmount());
        return budgetCategoryRepository.save(budgetCategory);
    }

    @Override
    public Iterable<BudgetCategory> createAllForCategory(Category category) {
        List<BudgetCategory> budgetCategories =
                StreamSupport.stream(budgetRepository.findAll().spliterator(), false)
                .map(budget -> new BudgetCategory(null, budget, category, 0))
                .collect(Collectors.toList());
        return budgetCategoryRepository.saveAll(budgetCategories);
    }
}
