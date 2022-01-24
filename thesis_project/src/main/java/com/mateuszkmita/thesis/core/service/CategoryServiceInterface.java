package com.mateuszkmita.thesis.core.service;

import com.mateuszkmita.thesis.core.exception.ResourceNotFoundException;
import com.mateuszkmita.thesis.core.interactor.CategoryInteractor;
import com.mateuszkmita.thesis.model.Category;

import java.util.Optional;

public interface CategoryServiceInterface {
    Optional<Category> findCategoryById(int id);
    Iterable<Category> findAllCategories();
    Optional<Category> findCategoryByName(String name);
    CategoryInteractor.CategoryCreationResult saveCategoryEntity(Category entity);
    Category updateCategoryEntity(Category updatedEntity);
    void deleteCategoryById(int id) throws ResourceNotFoundException;
    Category getIncomesCategory();
}
