package com.mateuszkmita.thesis.external.controller.dto.budget;

import com.mateuszkmita.thesis.external.controller.dto.category.CategoryDto;

public record BudgetCategoryDto(int id, CategoryDto category, int amount, int spent, int available) {
}
