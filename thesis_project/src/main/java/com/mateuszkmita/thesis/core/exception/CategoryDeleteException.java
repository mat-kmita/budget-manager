package com.mateuszkmita.thesis.core.exception;

import com.mateuszkmita.thesis.model.Category;

public class CategoryDeleteException extends RuntimeException{
    public CategoryDeleteException(Category category) {
        super("Cannot delete category " + category.getName() + " because there are existing transactions");
    }
}
