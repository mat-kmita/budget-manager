package com.mateuszkmita.thesis.external.controller.dto.report;

import com.mateuszkmita.thesis.external.repository.dto.TransactionAmountSumByDateDto;

import java.util.List;

public record HeatmapReportDto(List<TransactionAmountSumByDateDto> amountsByDate) {
}
