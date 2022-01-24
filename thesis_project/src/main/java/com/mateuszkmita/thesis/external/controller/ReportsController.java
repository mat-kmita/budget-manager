package com.mateuszkmita.thesis.external.controller;

import com.mateuszkmita.thesis.core.service.TransactionServiceInterface;
import com.mateuszkmita.thesis.external.controller.dto.ExpensesByCategoryDto;
import com.mateuszkmita.thesis.external.controller.dto.report.HeatmapReportDto;
import com.mateuszkmita.thesis.external.controller.mapper.ReportsMapper;
import com.mateuszkmita.thesis.external.repository.dto.TransactionAmountSumByDateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/report/")
@CrossOrigin
@RequiredArgsConstructor
public class ReportsController {

    private final ReportsMapper reportsMapper;
    private final TransactionServiceInterface transactionService;

    @GetMapping("/heatmap/outcomes")
    public HeatmapReportDto getOutcomesHeatMapReport(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<TransactionAmountSumByDateDto> heatMapData = transactionService.findOutcomesAmountDailyByDate(startDate, endDate);
        return new HeatmapReportDto(heatMapData);
    }

    @GetMapping("/heatmap/incomes")
    public HeatmapReportDto getIncomesHeatMapReport(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<TransactionAmountSumByDateDto> heatMapData = transactionService.findIncomesAmountDailyByDate(startDate, endDate);
        return new HeatmapReportDto(heatMapData);
    }

    @GetMapping("/expenses-by-category")
    public List<ExpensesByCategoryDto> getExpansesByCategoryReport(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return transactionService
                .findExpensesCategorizedSumByDate(startDate, endDate)
                .entrySet().stream()
                .map(entry -> reportsMapper.toDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

//    @GetMapping("/budget")
}