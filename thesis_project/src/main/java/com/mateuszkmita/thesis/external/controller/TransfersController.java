package com.mateuszkmita.thesis.external.controller;

import com.mateuszkmita.thesis.core.exception.ResourceNotFoundException;
import com.mateuszkmita.thesis.core.service.TransferServiceInterface;
import com.mateuszkmita.thesis.external.controller.dto.ProcedureResultDto;
import com.mateuszkmita.thesis.external.controller.dto.TransferDto;
import com.mateuszkmita.thesis.external.controller.dto.TransferUpdateDto;
import com.mateuszkmita.thesis.external.controller.mapper.TransferMapper;
import com.mateuszkmita.thesis.model.Transfer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/transfer/")
@RequiredArgsConstructor
public class TransfersController {

    private final TransferServiceInterface transferService;
    private final TransferMapper transferMapper;

    @PutMapping("{transferId}/")
    public ResponseEntity<TransferDto> updateTransfer(@PathVariable(name = "transferId") int transferId,
                                                      @RequestBody TransferUpdateDto dto) {
        Optional<Transfer> optionalTransfer = transferService.findTransferById(transferId);

        if (optionalTransfer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Transfer existingTransfer = optionalTransfer.get();
        Transfer existingTransferCopy = existingTransfer.copy();
        transferMapper.updateEntityByDto(dto, existingTransfer);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transferMapper.entityToDtoWithBothPayees(transferService.updateTransferEntity(existingTransferCopy, existingTransfer)));
    }

    @DeleteMapping("{transferId}/")
    public ResponseEntity<ProcedureResultDto> deleteTransfer(@PathVariable(name = "transferId") int transferId)
            throws ResourceNotFoundException {
        Transfer transfer = transferService.findTransferById(transferId)
                .orElseThrow(() -> new ResourceNotFoundException("transfer", transferId));

        transferService.deleteTransfer(transfer);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
