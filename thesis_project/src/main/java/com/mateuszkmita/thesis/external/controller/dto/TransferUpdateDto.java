package com.mateuszkmita.thesis.external.controller.dto;

import java.time.LocalDate;

public class TransferUpdateDto {
    private LocalDate date;
    private String memo;
    private Integer amount;
    private Integer fromAccountId;
    private Integer toAccountId;
}
