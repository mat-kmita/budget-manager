package com.mateuszkmita.thesis.external.controller.dto.budget;

public record BudgetDto(int id, int month, int year, int available, Integer nextBudgetId, Integer previousBudgetId) { }
