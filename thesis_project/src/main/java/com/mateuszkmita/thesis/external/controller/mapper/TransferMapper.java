package com.mateuszkmita.thesis.external.controller.mapper;

import com.mateuszkmita.thesis.external.controller.dto.NewTransferDto;
import com.mateuszkmita.thesis.external.controller.dto.TransferDto;
import com.mateuszkmita.thesis.external.controller.dto.TransferDtoWithBothPayees;
import com.mateuszkmita.thesis.external.controller.dto.TransferUpdateDto;
import com.mateuszkmita.thesis.model.Account;
import com.mateuszkmita.thesis.model.Transfer;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TransferMapper {

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "date", source = "entity.date", dateFormat = "dd-MM-yyyy")
    @Mapping(target = "memo", source = "entity.memo")
    @Mapping(target = "amount", source = "entity.amount")
    @Mapping(target = "payee", source = "payeeAccount.name")
    TransferDto entityToDto(Transfer entity, Account payeeAccount);


    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "date", source = "entity.date", dateFormat = "dd-MM-yyyy")
    @Mapping(target = "memo", source = "entity.memo")
    @Mapping(target = "amount", source = "entity.amount")
    @Mapping(target = "payee", source = "entity.account.name")
    @Mapping(target = "secondPayee", source = "entity.secondAccount.name")
    TransferDtoWithBothPayees entityToDtoWithBothPayees(Transfer entity);

    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "date", source = "dto.date", dateFormat = "dd-MM-yyyy")
    @Mapping(target = "memo", source = "dto.memo")
    @Mapping(target = "amount", source = "dto.amount")
    @Mapping(target = "account", source = "fromAccount")
    @Mapping(target = "secondAccount", source = "toAccount")
    Transfer newDtoToEntity(NewTransferDto dto, Account fromAccount, Account toAccount);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityByDto(TransferUpdateDto dto, @MappingTarget Transfer transfer);
}
