package com.mateuszkmita.thesis.external.controller.mapper;

import com.mateuszkmita.thesis.external.controller.dto.AccountUpdateDto;
import com.mateuszkmita.thesis.model.Account;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AccountUpdateMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAccountEntity(AccountUpdateDto dto, @MappingTarget Account account);
}
