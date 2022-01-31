package com.mateuszkmita.thesis.external.controller.dto.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mateuszkmita.thesis.model.AccountType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public record NewAccountDto(@NotNull String name,
                            String description,
                            @PositiveOrZero Integer initialBalance,
                            @NotNull @JsonFormat(shape = JsonFormat.Shape.STRING) AccountType accountType) { }
