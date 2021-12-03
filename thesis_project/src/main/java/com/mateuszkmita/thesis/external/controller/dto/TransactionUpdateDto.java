package com.mateuszkmita.thesis.external.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TransactionUpdateDto {
    private LocalDate date;
    private String memo;
    private Integer amount;
    private String payee;
}
