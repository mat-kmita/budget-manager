package com.mateuszkmita.thesis.external.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class NewTransactionDto {
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;
    private String memo;
    private int amount;
    private String payee;
}
