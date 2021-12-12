package com.mateuszkmita.thesis.external.controller.dto.budget;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public record NewBudgetDto(@Min(1) @Max(12) @NotNull Integer month, @NotNull Integer year) {
}
