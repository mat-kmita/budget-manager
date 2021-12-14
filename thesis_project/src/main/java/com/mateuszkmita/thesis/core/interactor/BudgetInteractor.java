package com.mateuszkmita.thesis.core.interactor;

import com.mateuszkmita.thesis.core.exception.ResourceNotFoundException;
import com.mateuszkmita.thesis.core.service.BudgetCategoryServiceInterface;
import com.mateuszkmita.thesis.core.service.BudgetServiceInterface;
import com.mateuszkmita.thesis.core.service.CategoryServiceInterface;
import com.mateuszkmita.thesis.core.service.TransactionServiceInterface;
import com.mateuszkmita.thesis.external.repository.BudgetRepositoryInterface;
import com.mateuszkmita.thesis.model.Budget;
import com.mateuszkmita.thesis.model.BudgetCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class BudgetInteractor implements BudgetServiceInterface {

    private final BudgetRepositoryInterface budgetRepository;
    private final CategoryServiceInterface categoryService;
    private final TransactionServiceInterface transactionService;
    private final BudgetCategoryServiceInterface budgetCategoryService;

    @Override
    public Optional<Budget> findBudget(int month, int year) {
        return budgetRepository.findByMonthAndYear(month, year);
    }

    @Override
    public Optional<Budget> findBudget(int id) {
        return budgetRepository.findById(id);
    }

    @Override
    @Transactional
    public Budget createBudget(int month, int year) {
        // Allow only one budget per month and year
        Optional<Budget> optionalBudget = budgetRepository.findByMonthAndYear(month, year);
        if (optionalBudget.isPresent()) {
            throw new IllegalArgumentException("Budget for " + month + " " + year +" already exists!");
        }

        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);

        Budget budget = new Budget(null, firstDayOfMonth, 0, null);
        List<BudgetCategory> allCategories = StreamSupport
                .stream(categoryService.findAllCategories().spliterator(), true)
                .map(category -> new BudgetCategory(null, budget, category, 0, 0, 0))
                .collect(Collectors.toList());
        budget.setBudgetCategories(allCategories);

        var persisted = budgetRepository.save(budget);
        try {
            int x = calculateAmountAvailable(budget.getId());
            persisted.setAvailable(x);

            persisted.setBudgetCategories(persisted.getBudgetCategories().stream()
                    .map(budgetCategoryService::calculateBudgetCategoryAmounts)
                    .collect(Collectors.toList()));

            budgetRepository.save(persisted);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return persisted;
    }

    @Override
    public int calculateAmountAvailable(int budgetId)
            throws ResourceNotFoundException {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("budget", budgetId));
        Optional<Budget> optionalPreviousBudget = budgetRepository
                .findFirstByDateBeforeOrderByDateDesc(budget.getDate());
        int previousBudgetBalance = optionalPreviousBudget.map(Budget::getAvailable).orElse(0);
        int previousBudgetOverspentCategoriesSum = optionalPreviousBudget
                .map(Budget::getBudgetCategories)
                .map(categories -> categories.stream()
                        .map(BudgetCategory::getBalance)
                        .filter(integer -> integer < 0)
                        .reduce(0, Integer::sum))
                .orElse(0);
        int thisMonthIncome = transactionService.calculateIncomeByMonthAndYear(budget.getDate().getMonthValue(),
                budget.getDate().getYear());
        int thisMonthBudgeted = budget.getBudgetCategories().stream()
                .map(BudgetCategory::getAmount)
                .reduce(0, Integer::sum);

        return previousBudgetBalance + previousBudgetOverspentCategoriesSum + thisMonthIncome - thisMonthBudgeted;
    }
}
