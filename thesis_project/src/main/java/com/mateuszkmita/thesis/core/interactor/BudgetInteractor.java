package com.mateuszkmita.thesis.core.interactor;

import com.mateuszkmita.thesis.core.service.BudgetServiceInterface;
import com.mateuszkmita.thesis.core.service.CategoryServiceInterface;
import com.mateuszkmita.thesis.external.repository.BudgetRepositoryInterface;
import com.mateuszkmita.thesis.model.Budget;
import com.mateuszkmita.thesis.model.BudgetCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    @Override
    public Optional<Budget> findBudget(int month, int year) {
        return budgetRepository.findByMonthAndYear(month, year);
    }

    @Override
    public Optional<Budget> findBudget(int id) {
        return budgetRepository.findById(id);
    }

    @Override
    public Budget createBudget(int month, int year) {
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        Budget budget = new Budget(null, firstDayOfMonth, 0, null);
        List<BudgetCategory> allCategories = StreamSupport.stream(categoryService.findAllCategories().spliterator(), true)
                .map(category -> new BudgetCategory(null, budget, category, 0, 0, 0))
                .collect(Collectors.toList());
        budget.setBudgetCategories(allCategories);

        return budgetRepository.save(budget);
    }
}
