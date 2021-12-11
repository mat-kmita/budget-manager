package com.mateuszkmita.thesis.external.controller.dto.category;

import javax.validation.constraints.NotNull;

public record NewCategoryDto(@NotNull String name) {}
