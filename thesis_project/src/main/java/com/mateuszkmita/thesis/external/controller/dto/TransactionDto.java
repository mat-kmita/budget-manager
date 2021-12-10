package com.mateuszkmita.thesis.external.controller.dto;

import java.time.LocalDate;

public record TransactionDto(int id, LocalDate date, String memo, int amount, String payee, String category) {}
