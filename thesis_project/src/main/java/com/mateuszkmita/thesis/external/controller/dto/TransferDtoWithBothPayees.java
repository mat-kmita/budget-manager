package com.mateuszkmita.thesis.external.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferDtoWithBothPayees extends TransferDto {
    private String secondPayee;
}
