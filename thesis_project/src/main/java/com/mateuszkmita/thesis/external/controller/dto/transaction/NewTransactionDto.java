package com.mateuszkmita.thesis.external.controller.dto.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

public record NewTransactionDto(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @PastOrPresent(message = "Future transaction are not supported")
        @NotNull
        LocalDate date,

        String memo,
        @NotNull Integer amount,
        @NotNull String payee,
        @NotNull Integer categoryId) {
}
