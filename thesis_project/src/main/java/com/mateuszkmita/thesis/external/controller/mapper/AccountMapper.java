package com.mateuszkmita.thesis.external.controller.mapper;

import com.mateuszkmita.thesis.external.controller.dto.account.AccountDto;
import com.mateuszkmita.thesis.external.controller.dto.account.AccountUpdateDto;
import com.mateuszkmita.thesis.external.controller.dto.account.NewAccountDto;
import com.mateuszkmita.thesis.model.Account;
import org.mapstruct.*;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AccountMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAccountEntity(AccountUpdateDto dto, @MappingTarget Account account);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "accountType", source = "accountType")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "balance", source = "balance")
    AccountDto toDto(Account account);

    @Mapping(target = "name", source = "name")
    @Mapping(target = "accountType", source = "accountType")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "balance", source = "initialBalance")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Account toEntity(NewAccountDto dto);
}
