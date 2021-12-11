package com.mateuszkmita.thesis.external.controller.dto.account;

import com.mateuszkmita.thesis.model.AccountType;

public record AccountUpdateDto (
    String description,
    String name,
    AccountType accountType
) {}
