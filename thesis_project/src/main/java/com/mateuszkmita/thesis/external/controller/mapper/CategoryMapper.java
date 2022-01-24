package com.mateuszkmita.thesis.external.controller.mapper;

import com.mateuszkmita.thesis.external.controller.dto.category.CategoryDto;
import com.mateuszkmita.thesis.external.controller.dto.category.CategoryUpdateDto;
import com.mateuszkmita.thesis.external.controller.dto.category.NewCategoryDto;
import com.mateuszkmita.thesis.model.BudgetCategory;
import com.mateuszkmita.thesis.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {BudgetMapper.class})
public interface CategoryMapper {

//    @Mapping(target = "id", source = "entity.id")
//    @Mapping(target = "name", source = "entity.name")
//    @Mapping(target = "budget)
    CategoryDto toDto(Category entity, Iterable<BudgetCategory> budgetCategories);

    CategoryDto toDto(Category entity);

    Iterable<CategoryDto> toDto(Iterable<Category> entities);

    Category newCategoryDtoToEntity(NewCategoryDto dto);

    Category updatedCategoryDtoToEntity(CategoryUpdateDto dto);
}
