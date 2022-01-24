package com.mateuszkmita.thesis.external.repository.dto;

import java.time.LocalDate;

public record TransactionAmountSumByDateDto(LocalDate date, Long sum) { }
