package com.mateuszkmita.thesis.core.interactor;

import com.mateuszkmita.thesis.core.service.BudgetServiceInterface;
import com.mateuszkmita.thesis.core.service.CategoryServiceInterface;
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
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class BudgetInteractor implements BudgetServiceInterface {

    private final BudgetRepositoryInterface budgetRepository;
    private final CategoryServiceInterface categoryService;

    @Override
    public Stream<Budget> findAllSortedByDate() {
        return StreamSupport.stream(budgetRepository.findAllByOrderByDateDesc().spliterator(), false);
    }

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
        Optional<Budget> previousBudget = budgetRepository.findFirstByDateBeforeOrderByDateDesc(firstDayOfMonth);
        Optional<Budget> nextBudget = budgetRepository.findFirstByDateAfterOrderByDateAsc(firstDayOfMonth);

        Budget budget = new Budget(null,
                firstDayOfMonth,
                null,
                previousBudget.orElse(null),
                nextBudget.orElse(null),
                0);
        List<BudgetCategory> budgetCategories =
                StreamSupport.stream(categoryService.findAllCategories().spliterator(), false)
                .map(c -> new BudgetCategory(null, budget, c, 0))
                .collect(Collectors.toList());
        budget.setBudgetCategories(budgetCategories);
        nextBudget.ifPresent(b -> b.setPreviousBudget(budget));
        Budget persistedBudget = budgetRepository.save(budget);
        nextBudget.ifPresent(budgetRepository::save);

        return budgetRepository.save(persistedBudget);
    }
}
