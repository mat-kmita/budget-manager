package com.mateuszkmita.thesis.external.controller.dto.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mateuszkmita.thesis.external.controller.dto.budget.BudgetCategoryDto;

public record CategoryDto(int id, String name,
                          @JsonInclude(JsonInclude.Include.NON_NULL) Iterable<BudgetCategoryDto> budgetCategories) {}
