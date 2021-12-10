package com.mateuszkmita.thesis.external.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record NewTransactionDto(@JsonFormat(pattern = "dd-MM-yyyy") LocalDate date,
                                String memo, int amount, String payee, int categoryId) {}
