package com.mateuszkmita.thesis.external.controller.dto.account;

import com.mateuszkmita.thesis.model.AccountType;

public record AccountDto(int id, String name, AccountType accountType, String description, int balance) {
}
