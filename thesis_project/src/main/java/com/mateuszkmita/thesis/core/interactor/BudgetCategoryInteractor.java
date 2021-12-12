package com.mateuszkmita.thesis.core.interactor;

import com.mateuszkmita.thesis.core.service.BudgetCategoryServiceInterface;
import com.mateuszkmita.thesis.external.repository.BudgetCategoryRepositoryInterface;
import com.mateuszkmita.thesis.model.BudgetCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetCategoryInteractor implements BudgetCategoryServiceInterface {

    private final BudgetCategoryRepositoryInterface budgetCategoryRepository;

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
}
