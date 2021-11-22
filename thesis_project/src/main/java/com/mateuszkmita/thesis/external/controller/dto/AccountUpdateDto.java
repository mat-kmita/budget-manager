package com.mateuszkmita.thesis.external.controller.dto;

import com.mateuszkmita.thesis.model.AccountType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountUpdateDto {
    private String description;
    private String name;
    private AccountType accountType;
}
