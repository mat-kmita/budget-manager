package com.mateuszkmita.thesis.external.controller.dto.transfer;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Past;
import java.time.LocalDate;

public record TransferUpdateDto(
        @JsonFormat(pattern = "dd-MM-yyyy")
        @Past
        LocalDate date,
        String memo,
        Integer amount,
        Integer fromAccountId,
        Integer toAccountId) {
}
