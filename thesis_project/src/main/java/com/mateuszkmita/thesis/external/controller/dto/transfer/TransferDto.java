package com.mateuszkmita.thesis.external.controller.dto.transfer;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class TransferDto {
    private int id;
    private LocalDate date;
    private String memo;
    private int amount;
    private String payee;
}
