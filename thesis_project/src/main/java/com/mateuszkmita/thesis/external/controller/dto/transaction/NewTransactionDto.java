package com.mateuszkmita.thesis.external.controller.dto.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record NewTransactionDto(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @Past(message = "Future transaction are not supported")
        @NotNull
        LocalDate date,

        String memo,
        @NotNull Integer amount,
        @NotNull String payee,
        @NotNull Integer categoryId) {
}
