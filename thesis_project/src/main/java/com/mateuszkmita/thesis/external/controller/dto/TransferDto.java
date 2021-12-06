package com.mateuszkmita.thesis.external.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TransferDto {
    private int id;
    private LocalDate date;
    private String memo;
    private int amount;
    private String payee;
}
