package com.mateuszkmita.thesis.external.controller.dto.transfer;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TransferDtoWithBothPayees extends TransferDto {
    private int secondPayeeId;

    public TransferDtoWithBothPayees(int id, LocalDate date, String memo, int amount, int payeeId, int secondPayeeId) {
        super(id, date, memo, amount, payeeId);
        this.secondPayeeId = secondPayeeId;
    }
}
