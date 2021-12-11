package com.mateuszkmita.thesis.external.controller;

import com.mateuszkmita.thesis.core.exception.ResourceNotFoundException;
import com.mateuszkmita.thesis.core.service.CategoryServiceInterface;
import com.mateuszkmita.thesis.external.controller.dto.category.CategoryDto;
import com.mateuszkmita.thesis.external.controller.dto.category.CategoryUpdateDto;
import com.mateuszkmita.thesis.external.controller.dto.category.NewCategoryDto;
import com.mateuszkmita.thesis.external.controller.mapper.CategoryMapper;
import com.mateuszkmita.thesis.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/category/")
@RequiredArgsConstructor
public class CategoriesController {

    private final CategoryServiceInterface categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping("")
    public Iterable<CategoryDto> getCategories() {
        return categoryMapper.toDto(categoryService.findAllCategories());
    }

    @PostMapping("")
    public ResponseEntity<CategoryDto> addCategory(@RequestBody @Valid NewCategoryDto requestDto) {
        Category category = categoryService.saveCategoryEntity(categoryMapper.newCategoryDtoToEntity(requestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryMapper.toDto(category));
    }

    @PutMapping("/{categoryId}/")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody @Valid CategoryUpdateDto requestDto,
                                                      @PathVariable(name = "categoryId") int id) {
        Category category = categoryMapper.updatedCategoryDtoToEntity(requestDto);
        category.setId(id);
        Category updatedCategory = categoryService.updateCategoryEntity(category);
        return ResponseEntity.status(HttpStatus.OK).body(categoryMapper.toDto(updatedCategory));
    }

    @DeleteMapping("/{categoryId}/")
    public ResponseEntity<Void> deleteCategory(@PathVariable(name = "categoryId") int id)
            throws ResourceNotFoundException {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
