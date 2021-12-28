package com.mateuszkmita.thesis.external.controller;

import com.mateuszkmita.thesis.core.exception.ResourceNotFoundException;
import com.mateuszkmita.thesis.core.service.AccountServiceInterface;
import com.mateuszkmita.thesis.core.service.CategoryServiceInterface;
import com.mateuszkmita.thesis.core.service.TransactionServiceInterface;
import com.mateuszkmita.thesis.external.controller.dto.util.ProcedureResultDto;
import com.mateuszkmita.thesis.external.controller.dto.transaction.TransactionDto;
import com.mateuszkmita.thesis.external.controller.dto.transaction.TransactionUpdateDto;
import com.mateuszkmita.thesis.external.controller.mapper.TransactionMapper;
import com.mateuszkmita.thesis.model.Category;
import com.mateuszkmita.thesis.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/transaction/")
@RequiredArgsConstructor
@CrossOrigin
public class TransactionsController {

    private final TransactionServiceInterface transactionService;
    private final TransactionMapper transactionMapper;
    private final CategoryServiceInterface categoryService;
    private final AccountServiceInterface accountService;

    @PutMapping("/{transactionId}/")
    public ResponseEntity<TransactionDto> updateTransaction(@PathVariable(name = "transactionId") int transactionId,
                                                            @RequestBody @Valid TransactionUpdateDto dto) throws ResourceNotFoundException {
        Transaction oldTransaction = transactionService.findTransactionById(transactionId)
                        .orElseThrow(() -> new ResourceNotFoundException("transaction", transactionId));
        Transaction updatedTransaction = oldTransaction.copy();
        Category updatedCategory = Optional.ofNullable(dto.categoryId()).flatMap(categoryService::findCategoryById).orElse(null);
        transactionMapper.updateEntityByDto(dto, updatedTransaction, updatedCategory);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactionMapper.entityToDto(transactionService.updateTransactionEntity(oldTransaction, updatedTransaction)));
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
