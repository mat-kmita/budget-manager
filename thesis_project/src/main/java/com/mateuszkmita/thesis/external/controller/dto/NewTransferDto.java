package com.mateuszkmita.thesis.external.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Getter
@Setter
public class NewTransferDto {
    @JsonFormat(pattern = "dd-MM-yyyy")
    @PastOrPresent(message = "Transfer date must be from the past!")
    private LocalDate date;
    private String memo;
    @Positive(message = "Transfer amount must be positive!")
    private int amount;
    @NotNull(message = "Missing to account id!")
    private Integer toAccountId;
}
