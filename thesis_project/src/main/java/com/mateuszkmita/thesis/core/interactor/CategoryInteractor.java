package com.mateuszkmita.thesis.core.interactor;

import com.mateuszkmita.thesis.core.exception.ResourceNotFoundException;
import com.mateuszkmita.thesis.core.service.CategoryServiceInterface;
import com.mateuszkmita.thesis.external.repository.CategoryRepositoryInterface;
import com.mateuszkmita.thesis.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryInteractor implements CategoryServiceInterface {

    private final CategoryRepositoryInterface categoryRepository;
    private static Category INCOMES_CATEGORY = null;

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
    public Category saveCategoryEntity(Category entity) {
        if (entity.getId() != null) {
            throw new IllegalArgumentException("New category must not have an ID!");
        }

        return categoryRepository.save(entity);
    }

    @Override
    public Category updateCategoryEntity(Category updatedEntity) {
        if (updatedEntity.getId() == null) {
            throw new IllegalArgumentException("Updated category must have an ID!");
        }

        if (Objects.equals(updatedEntity.getId(), INCOMES_CATEGORY.getId())) {
            throw new IllegalArgumentException("Category " + INCOMES_CATEGORY.getName() + " cannot be updated!");
        }

        return categoryRepository.save(updatedEntity);
    }

    @Override
    public void deleteCategoryById(int id) throws ResourceNotFoundException {
        if (id == INCOMES_CATEGORY.getId()) {
            throw new IllegalArgumentException("Category " + INCOMES_CATEGORY.getName() + " cannot be updated!");
        }

        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            throw new ResourceNotFoundException("category", id);
        }

        categoryRepository.delete(optionalCategory.get());
    }

    @Override
    public Category getIncomesCategory() {
        if (INCOMES_CATEGORY == null) {
            INCOMES_CATEGORY = categoryRepository.findIncomesCategory()
                    .orElseThrow(() -> new IllegalStateException("Incomes category doesn't exist!"));
        }

        return INCOMES_CATEGORY;
    }
}
