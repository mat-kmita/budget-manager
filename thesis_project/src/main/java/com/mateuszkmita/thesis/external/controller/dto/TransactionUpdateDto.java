package com.mateuszkmita.thesis.external.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record TransactionUpdateDto(@JsonFormat(pattern = "dd-MM-yyyy") LocalDate date,
                                   String memo, Integer amount, String payee, Integer categoryId) {}
