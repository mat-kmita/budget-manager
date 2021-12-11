package com.mateuszkmita.thesis.external.controller.dto.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

public record NewTransactionDto(
        @JsonFormat(pattern = "dd-MM-yyyy") @Past(message = "Future transaction are not supported") @NotNull
        LocalDate date,
        String memo,
        @NotNull Integer amount,
        @NotNull String payee,
        @NotNull Integer categoryId) {
}
