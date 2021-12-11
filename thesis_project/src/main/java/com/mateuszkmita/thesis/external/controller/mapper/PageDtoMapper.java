package com.mateuszkmita.thesis.external.controller.mapper;

import com.mateuszkmita.thesis.external.controller.dto.page.PageDto;
import org.springframework.data.domain.Page;

public interface PageDtoMapper {

    default <T> PageDto<T> toDto(Page<T> pageEntity) {
        if (pageEntity == null) {
            return null;
        }

        return new PageDto<>(pageEntity.getContent(), pageEntity.getNumber(), pageEntity.getTotalPages(),
                pageEntity.getNumberOfElements());
    }
}
