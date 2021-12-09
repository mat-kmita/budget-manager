package com.mateuszkmita.thesis.external.controller;

import com.mateuszkmita.thesis.core.exception.ResourceNotFoundException;
import com.mateuszkmita.thesis.core.service.TransactionServiceInterface;
import com.mateuszkmita.thesis.external.controller.dto.ProcedureResultDto;
import com.mateuszkmita.thesis.external.controller.dto.TransactionDto;
import com.mateuszkmita.thesis.external.controller.dto.TransactionUpdateDto;
import com.mateuszkmita.thesis.external.controller.mapper.TransactionMapper;
import com.mateuszkmita.thesis.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/transaction/")
@RequiredArgsConstructor
public class TransactionsController {

    private final TransactionServiceInterface transactionService;
    private final TransactionMapper transactionMapper;

    @PutMapping("/{transactionId}/")
    public ResponseEntity<TransactionDto> updateTransaction(@PathVariable(name = "transactionId") int transactionId,
                                                            @RequestBody TransactionUpdateDto dto) {
        Optional<Transaction> optionalTransaction = transactionService.findTransactionById(transactionId);

        if (optionalTransaction.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Transaction existingTransaction = optionalTransaction.get();
        Transaction existingTransactionCopy = existingTransaction.copy();
        transactionMapper.updateEntityByDto(dto, existingTransaction);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionMapper.entityToDto(transactionService.updateTransactionEntity(existingTransactionCopy, existingTransaction)));
    }

    @DeleteMapping("{transactionId}")
    public ResponseEntity<ProcedureResultDto> deleteTransaction(@PathVariable(name = "transactionId") int transactionId)
            throws ResourceNotFoundException {
        Transaction transaction = transactionService.findTransactionById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("transaction", transactionId));

        transactionService.deleteTransaction(transaction);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
