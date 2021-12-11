package com.mateuszkmita.thesis.external.controller.dto.transfer;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TransferDtoWithBothPayees extends TransferDto {
    private String secondPayee;

    public TransferDtoWithBothPayees(int id, LocalDate date, String memo, int amount, String payee, String secondPayee) {
        super(id, date, memo, amount, payee);
        this.secondPayee = secondPayee;
    }
}
