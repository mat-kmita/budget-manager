package com.mateuszkmita.thesis.external.controller.mapper;

import com.mateuszkmita.thesis.external.controller.dto.AccountUpdateDto;
import com.mateuszkmita.thesis.external.controller.dto.NewTransactionDto;
import com.mateuszkmita.thesis.external.controller.dto.TransactionDto;
import com.mateuszkmita.thesis.external.controller.dto.TransactionUpdateDto;
import com.mateuszkmita.thesis.model.Account;
import com.mateuszkmita.thesis.model.Transaction;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "date", source = "date", dateFormat = "dd-MM-yyyy") //TODO format date
    @Mapping(target = "memo", source = "memo")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "payee", source = "transaction.account.name") // TODO check if it works. Otherwise use named method
    TransactionDto entityToDto(Transaction transaction);

    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "date", source = "dto.date", dateFormat = "dd-MM-yyyy")
    @Mapping(target = "memo", source = "dto.memo")
    @Mapping(target = "amount", source = "dto.amount")
    @Mapping(target = "payee", source = "dto.payee")
    @Mapping(target = "account", source = "account")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Transaction newTransactionDtoToEntity(NewTransactionDto dto, Account account);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityByDto(TransactionUpdateDto dto, @MappingTarget Transaction transaction);

}
