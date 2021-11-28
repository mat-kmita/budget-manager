package com.mateuszkmita.thesis.external.controller;

import com.mateuszkmita.thesis.core.exception.ResourceNotFoundException;
import com.mateuszkmita.thesis.core.service.AccountServiceInterface;
import com.mateuszkmita.thesis.core.service.TransactionServiceInterface;
import com.mateuszkmita.thesis.external.controller.dto.*;
import com.mateuszkmita.thesis.external.controller.mapper.AccountUpdateMapper;
import com.mateuszkmita.thesis.external.controller.mapper.PageDtoMapper;
import com.mateuszkmita.thesis.external.controller.mapper.TransactionMapper;
import com.mateuszkmita.thesis.model.Account;
import com.mateuszkmita.thesis.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/api/v1/account/")
public class AccountsController {

    private final AccountServiceInterface accountService;
    private final AccountUpdateMapper accountUpdateMapper;
    private final TransactionMapper transactionMapper;
    private final TransactionServiceInterface transactionService;
    private final PageDtoMapper pageDtoMapper;

    @GetMapping("/")
    public Iterable<Account> getAllAccounts() {
        return accountService.findAllAccounts();
    }

    @PostMapping("/")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) { // TODO Create AccountDto
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.saveAccountEntity(account));
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<Account> updateAccount(@PathVariable(name = "accountId") int accountId, @RequestBody AccountUpdateDto accountUpdateDto)
            throws ResourceNotFoundException { //TODO Use AccountDto
        Optional<Account> existingAccount = accountService.findAccountById(accountId);

        if (existingAccount.isEmpty()) {
            throw new ResourceNotFoundException("account", accountId);
        }

        Account account = existingAccount.get();
        accountUpdateMapper.updateAccountEntity(accountUpdateDto, account);
        return ResponseEntity.status(HttpStatus.OK).body(accountService.updateAccountEntity(account));
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<ProcedureResultDto> deleteAccount(@PathVariable(name = "accountId") int accountId)
            throws ResourceNotFoundException {
        accountService.deleteAccountById(accountId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{accountId}/transaction/")
    public PageDto<TransactionDto> getTransactionsPage(@PathVariable int accountId,
                                                @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                @RequestParam(name = "length", required = false, defaultValue = "100") int length,
                                                @RequestParam(name = "sortField") Transaction.Fields sortField,
                                                @RequestParam(name = "sortDirection") Sort.Direction direction) {
        return pageDtoMapper.toDto(transactionService
                .findTransactionsByAccountId(accountId, sortField, direction, page, length)
                .map(transactionMapper::entityToDto));
    }

    @PostMapping("/{accountId}/transaction/")
    public ResponseEntity<TransactionDto> addTransaction(@RequestBody NewTransactionDto transactionDto,
                                                         @PathVariable(name = "accountId") int accountId) {

        Optional<Account> account = accountService.findAccountById(accountId);
        if (account.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Transaction newTransaction = transactionMapper.newTransactionDtoToEntity(transactionDto, account.get());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transactionMapper.entityToDto(transactionService.saveTransactionEntity(newTransaction)));
    }
}
