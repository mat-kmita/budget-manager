package com.mateuszkmita.thesis.external.controller.mapper;

import com.mateuszkmita.thesis.external.controller.dto.budget.BudgetCategoryDto;
import com.mateuszkmita.thesis.external.controller.dto.budget.BudgetDto;
import com.mateuszkmita.thesis.model.Budget;
import com.mateuszkmita.thesis.model.BudgetCategory;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface BudgetMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "month", expression = "java(budget.getDate().getMonthValue())")
    @Mapping(target = "year", source = "budget.date.year")
    @Mapping(target = "available", expression = "java(budget.available())")
    @Mapping(target = "previousBudgetId", source = "budget.previousBudget.id")
    @Mapping(target = "nextBudgetId", source = "budget.nextBudget.id")
    BudgetDto toDto(Budget budget);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "spent", source = "spent")
    @Mapping(target = "available", expression = "java(budgetCategory.available())")
    BudgetCategoryDto toDto(BudgetCategory budgetCategory);

    Iterable<BudgetCategoryDto> toDto(Iterable<BudgetCategory> budgetCategories);
}
