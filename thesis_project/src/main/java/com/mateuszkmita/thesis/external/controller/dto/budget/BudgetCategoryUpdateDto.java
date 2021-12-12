package com.mateuszkmita.thesis.external.controller.dto.budget;

import javax.validation.constraints.NotNull;

public record   BudgetCategoryUpdateDto(@NotNull Integer budgetedAmount) {
}
