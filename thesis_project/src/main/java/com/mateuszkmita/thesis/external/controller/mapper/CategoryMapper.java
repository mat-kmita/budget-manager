package com.mateuszkmita.thesis.external.controller.mapper;

import com.mateuszkmita.thesis.external.controller.dto.CategoryDto;
import com.mateuszkmita.thesis.external.controller.dto.NewCategoryDto;
import com.mateuszkmita.thesis.external.controller.dto.UpdatedCategoryDto;
import com.mateuszkmita.thesis.model.Category;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CategoryMapper {

    CategoryDto toDto(Category entity);

    Iterable<CategoryDto> toDto(Iterable<Category> entities);

    Category newCategoryDtoToEntity(NewCategoryDto dto);

    Category updatedCategoryDtoToEntity(UpdatedCategoryDto dto);
}
