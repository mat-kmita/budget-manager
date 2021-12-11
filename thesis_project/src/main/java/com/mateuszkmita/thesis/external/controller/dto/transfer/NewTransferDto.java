package com.mateuszkmita.thesis.external.controller.dto.transfer;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

public record NewTransferDto(
        @JsonFormat(pattern = "dd-MM-yyyy")
        @PastOrPresent(message = "Transfer date must be from the past!") @NotNull LocalDate date,
        String memo,
        @Positive(message = "Transfer amount must be positive!") Integer amount,
        @NotNull(message = "Missing to account id!") Integer toAccountId
) {
}
