package com.mateuszkmita.thesis.external.repository;

import com.mateuszkmita.thesis.model.BudgetCategory;
import com.mateuszkmita.thesis.model.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface BudgetCategoryRepositoryInterface extends CrudRepository<BudgetCategory, Integer> {
    Optional<BudgetCategory> findFirstByCategory_IdAndBudget_DateBeforeOrderByBudget_DateDesc(int categoryId, LocalDate date);
    Optional<BudgetCategory> findFirstByCategory_IdAndBudget_DateAfterOrderByBudget_DateAsc(int categoryId, LocalDate date);

    @Query(nativeQuery = true,
            value = """
                SELECT budgets_categories.* FROM budgets_categories
                JOIN budgets b on budgets_categories.budget_id = b.id
                WHERE category_id = :categoryId AND
                    EXTRACT(MONTH from b.month_year) = :month AND
                    EXTRACT(YEAR FROM b.month_year) = :year""")
    Optional<BudgetCategory> findByCategoryAndDate(int categoryId, int month, int year);
}
