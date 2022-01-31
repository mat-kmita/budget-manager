package com.mateuszkmita.thesis.external.controller.dto.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

public record TransactionUpdateDto(
        @JsonFormat(pattern = "dd-MM-yyyy") @PastOrPresent(message = "Future transactions are not supported!") LocalDate date,
        String memo, Integer amount, String payee, Integer categoryId) {}
