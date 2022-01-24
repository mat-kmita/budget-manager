package com.mateuszkmita.thesis.core.interactor;

import com.mateuszkmita.thesis.core.exception.CategoryDeleteException;
import com.mateuszkmita.thesis.core.exception.ResourceNotFoundException;
import com.mateuszkmita.thesis.core.service.BudgetCategoryServiceInterface;
import com.mateuszkmita.thesis.core.service.CategoryServiceInterface;
import com.mateuszkmita.thesis.external.repository.CategoryRepositoryInterface;
import com.mateuszkmita.thesis.external.repository.TransactionsRepositoryInterface;
import com.mateuszkmita.thesis.model.BudgetCategory;
import com.mateuszkmita.thesis.model.Category;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
public class CategoryInteractor implements CategoryServiceInterface {

    private final CategoryRepositoryInterface categoryRepository;
    private final BudgetCategoryServiceInterface budgetCategoryService;
    private final TransactionsRepositoryInterface transactionsRepository;

    private final Category incomesCategory;

    public CategoryInteractor(CategoryRepositoryInterface categoryRepository,
                              BudgetCategoryServiceInterface budgetCategoryService,
                              TransactionsRepositoryInterface transactionsRepository) {
        this.categoryRepository = categoryRepository;
        this.budgetCategoryService = budgetCategoryService;

        incomesCategory = categoryRepository.findIncomesCategory()
                .orElseThrow(() -> new IllegalStateException("Incomes category doesn't exist!"));
        this.transactionsRepository = transactionsRepository;
    }

    @Override
    public Optional<Category> findCategoryById(int id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Iterable<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    @Transactional
    public CategoryCreationResult saveCategoryEntity(Category entity) {
        if (entity.getId() != null) {
            throw new IllegalArgumentException("New category must not have an ID!");
        }

        Category persistedCategory = categoryRepository.save(entity);
        Iterable<BudgetCategory> persistedBudgetCategories = budgetCategoryService.createAllForCategory(entity);

        return new CategoryCreationResult(persistedCategory, persistedBudgetCategories);
    }

    @Override
    public Category updateCategoryEntity(Category updatedEntity) {
        if (updatedEntity.getId() == null) {
            throw new IllegalArgumentException("Updated category must have an ID!");
        }

        if (Objects.equals(updatedEntity.getId(), incomesCategory.getId())) {
            throw new IllegalArgumentException("Category " + incomesCategory.getName() + " cannot be updated!");
        }

        return categoryRepository.save(updatedEntity);
    }

    @Override
    @Transactional
    public void deleteCategoryById(int id) throws ResourceNotFoundException {
        if (id == incomesCategory.getId()) {
            throw new IllegalArgumentException("Category " + incomesCategory.getName() + " cannot be updated!");
        }

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("category", id));

        if (transactionsRepository.countByCategory_Id(category.getId()) > 0) {
            throw new CategoryDeleteException(category);
        }

        categoryRepository.delete(category);
    }

    @Override
    public Category getIncomesCategory() {
        return this.incomesCategory;
    }

    public record CategoryCreationResult(Category newCategory, Iterable<BudgetCategory> newBudgetCategories) {}
}
