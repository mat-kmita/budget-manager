package com.mateuszkmita.thesis.external.controller;

import com.mateuszkmita.thesis.core.exception.ResourceNotFoundException;
import com.mateuszkmita.thesis.core.service.BudgetCategoryServiceInterface;
import com.mateuszkmita.thesis.external.controller.dto.budget.BudgetCategoryDto;
import com.mateuszkmita.thesis.external.controller.dto.budget.BudgetCategoryUpdateDto;
import com.mateuszkmita.thesis.external.controller.mapper.BudgetMapper;
import com.mateuszkmita.thesis.model.BudgetCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/budgetCategory/")
@RequiredArgsConstructor
public class BudgetCategoriesController {

    private final BudgetCategoryServiceInterface budgetCategoryService;
    private final BudgetMapper budgetMapper;

    @PutMapping("/{budgetCategoryId}/")
    public BudgetCategoryDto updateBudgetCategory(@PathVariable int budgetCategoryId,
                                                  @RequestBody @Valid BudgetCategoryUpdateDto requestDto)
            throws ResourceNotFoundException {
        BudgetCategory existing = budgetCategoryService.findBudgetCategory(budgetCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("budget category", budgetCategoryId));
        existing.setAmount(requestDto.budgetedAmount());
        return budgetMapper.toDto(budgetCategoryService.updateBudgetCategoryEntity(existing));
    }
}
