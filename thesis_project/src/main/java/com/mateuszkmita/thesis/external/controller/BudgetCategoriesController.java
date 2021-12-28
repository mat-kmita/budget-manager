package com.mateuszkmita.thesis.external.controller;

import com.mateuszkmita.thesis.core.exception.ResourceNotFoundException;
import com.mateuszkmita.thesis.core.interactor.BudgetCategoryInteractor;
import com.mateuszkmita.thesis.core.service.BudgetServiceInterface;
import com.mateuszkmita.thesis.core.service.CategoryServiceInterface;
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

    private final BudgetCategoryInteractor budgetCategoryService;
    private final BudgetMapper budgetMapper;

    private final CategoryServiceInterface categoryService;
    private final BudgetServiceInterface budgetService;

    @PutMapping("/budget/{budgetId}/category/{categoryId}")
    public BudgetCategoryDto updateBudgetCategory(@PathVariable int budgetId,
                                                  @PathVariable int categoryId,
                                                  @RequestBody @Valid BudgetCategoryUpdateDto requestDto)
            throws ResourceNotFoundException {
        return budgetMapper.toDto(
                budgetCategoryService.updateBudgetCategoryAmountById(budgetId, categoryId, requestDto.budgetedAmount()));

    }
}
