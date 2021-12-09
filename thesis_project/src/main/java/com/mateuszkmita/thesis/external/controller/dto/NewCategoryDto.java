package com.mateuszkmita.thesis.external.controller.dto;

import javax.validation.constraints.NotNull;

public record NewCategoryDto(@NotNull String name) {}
