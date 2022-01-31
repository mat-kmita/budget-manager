package com.mateuszkmita.thesis.external.controller.mapper;

import com.mateuszkmita.thesis.external.controller.dto.transfer.NewTransferDto;
import com.mateuszkmita.thesis.external.controller.dto.transfer.TransferDto;
import com.mateuszkmita.thesis.external.controller.dto.transfer.TransferDtoWithBothPayees;
import com.mateuszkmita.thesis.external.controller.dto.transfer.TransferUpdateDto;
import com.mateuszkmita.thesis.model.Account;
import com.mateuszkmita.thesis.model.Transfer;
import org.mapstruct.*;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TransferMapper {

    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "date", source = "entity.date", dateFormat = "dd-MM-yyyy")
    @Mapping(target = "memo", source = "entity.memo")
    @Mapping(target = "amount", source = "entity.amount")
    @Mapping(target = "payeeId", source = "payeeAccount.id")
    TransferDto entityToDto(Transfer entity, Account payeeAccount);


    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "date", source = "entity.date", dateFormat = "dd-MM-yyyy")
    @Mapping(target = "memo", source = "entity.memo")
    @Mapping(target = "amount", source = "entity.amount")
    @Mapping(target = "payeeId", source = "entity.account.id")
    @Mapping(target = "secondPayeeId", source = "entity.secondAccount.id")
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
