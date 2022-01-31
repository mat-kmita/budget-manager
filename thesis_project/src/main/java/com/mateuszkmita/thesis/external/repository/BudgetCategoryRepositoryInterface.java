package com.mateuszkmita.thesis.external.repository;

import com.mateuszkmita.thesis.model.BudgetCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.stream.Stream;

public interface BudgetCategoryRepositoryInterface extends CrudRepository<BudgetCategory, Integer> {
    @Query(nativeQuery = true,
            value = """
                SELECT budgets_categories.* FROM budgets_categories
                JOIN budgets b on budgets_categories.budget_id = b.id
                WHERE category_id = :categoryId AND
                    EXTRACT(MONTH from b.month_year) = :month AND
                    EXTRACT(YEAR FROM b.month_year) = :year""")
    Optional<BudgetCategory> findByCategoryAndDate(int categoryId, int month, int year);

    Stream<BudgetCategory> findAllByCategory_Id(int categoryId);
}
