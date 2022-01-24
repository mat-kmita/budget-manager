package com.mateuszkmita.thesis.external.controller.mapper;

import com.mateuszkmita.thesis.external.controller.dto.ExpensesByCategoryDto;
import com.mateuszkmita.thesis.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReportsMapper {

    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "sum", source = "sum")
    ExpensesByCategoryDto toDto(Category category, Long sum);
}
