package com.mateuszkmita.thesis.external.repository.dto;

import com.mateuszkmita.thesis.model.Category;

public record ExpensesAmountSumWithCategory(Long sum, Category category) {}
