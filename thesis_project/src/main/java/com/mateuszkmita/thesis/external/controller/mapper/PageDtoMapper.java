package com.mateuszkmita.thesis.external.controller.mapper;

import com.mateuszkmita.thesis.external.controller.dto.PageDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

public interface PageDtoMapper {

    default <T> PageDto<T> toDto(Page<T> pageEntity) {
       if (pageEntity == null) {
           return null;
       }

       PageDto<T> pageDto = new PageDto<>();

       pageDto.setData(pageEntity.getContent());
       pageDto.setPage(pageEntity.getNumber());
       pageDto.setNumberOfPages(pageEntity.getTotalPages());
       pageDto.setAllDataSize(pageEntity.getNumberOfElements());

       return pageDto;
    }
}
