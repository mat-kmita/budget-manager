package com.mateuszkmita.thesis.external.controller;

import com.mateuszkmita.thesis.core.exception.ResourceNotFoundException;
import com.mateuszkmita.thesis.core.service.BudgetServiceInterface;
import com.mateuszkmita.thesis.external.controller.dto.budget.BudgetCategoryDto;
import com.mateuszkmita.thesis.external.controller.dto.budget.BudgetDto;
import com.mateuszkmita.thesis.external.controller.dto.budget.NewBudgetDto;
import com.mateuszkmita.thesis.external.controller.mapper.BudgetMapper;
import com.mateuszkmita.thesis.model.Budget;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/budget/")
@Validated
@RequiredArgsConstructor
@CrossOrigin
public class BudgetController {

    private final BudgetServiceInterface budgetService;
    private final BudgetMapper budgetMapper;

    @GetMapping("/")
    public List<BudgetDto> getAllBudgets() {
        return budgetService.findAllSortedByDate().map(budgetMapper::toDto).collect(Collectors.toList());
    }

//    @GetMapping("/")
//    public ResponseEntity<BudgetDto> getBudget(@RequestParam(name = "month") @Min(1) @Max(12) int month,
//                                               @RequestParam(name = "year") int year)
//            throws ResourceNotFoundException {
//        Budget budget = budgetService.findBudget(month, year)
//                .orElseThrow(() -> new ResourceNotFoundException("budget", 0));
//
//        BudgetDto result = budgetMapper.toDto(budget);
//        return ResponseEntity.status(HttpStatus.OK).body(result);
//    }

    @PostMapping("/")
    public BudgetDto createBudget(@RequestBody @Valid NewBudgetDto requestDto) {
        Budget budget = budgetService.createBudget(requestDto.month(), requestDto.year());
        return budgetMapper.toDto(budget);
    }

    @GetMapping("/{budgetId}/category/")
    public Iterable<BudgetCategoryDto> getBudgetCategories(@PathVariable(name = "budgetId") int budgetId)
            throws ResourceNotFoundException {
        Budget budget = budgetService.findBudget(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("budget", budgetId));

        return budget.getBudgetCategories().stream().map(budgetMapper::toDto).collect(Collectors.toList());
    }
}
