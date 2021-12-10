package com.mateuszkmita.thesis.external.controller;

import com.mateuszkmita.thesis.core.exception.ResourceNotFoundException;
import com.mateuszkmita.thesis.core.service.AccountServiceInterface;
import com.mateuszkmita.thesis.core.service.CategoryServiceInterface;
import com.mateuszkmita.thesis.core.service.TransactionServiceInterface;
import com.mateuszkmita.thesis.core.service.TransferServiceInterface;
import com.mateuszkmita.thesis.external.controller.dto.*;
import com.mateuszkmita.thesis.external.controller.mapper.AccountUpdateMapper;
import com.mateuszkmita.thesis.external.controller.mapper.PageDtoMapper;
import com.mateuszkmita.thesis.external.controller.mapper.TransactionMapper;
import com.mateuszkmita.thesis.external.controller.mapper.TransferMapper;
import com.mateuszkmita.thesis.model.Account;
import com.mateuszkmita.thesis.model.Category;
import com.mateuszkmita.thesis.model.Transaction;
import com.mateuszkmita.thesis.model.Transfer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    private final TransferMapper transferMapper;
    private final TransferServiceInterface transferService;
    private final CategoryServiceInterface categoryService;

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
                                                         @PathVariable(name = "accountId") int accountId)
            throws ResourceNotFoundException {

        Optional<Account> account = accountService.findAccountById(accountId);
        if (account.isEmpty()) {
            throw new ResourceNotFoundException("account", accountId);
        }

        Optional<Category> category = categoryService.findCategoryById(transactionDto.categoryId());
        if (category.isEmpty()) {
            throw new ResourceNotFoundException("category", transactionDto.categoryId());
        }

        Transaction newTransaction = transactionMapper.newTransactionDtoToEntity(transactionDto, account.get(), category.get());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transactionMapper.entityToDto(transactionService.saveTransactionEntity(newTransaction)));
    }

    @GetMapping("/{accountId}/transfer/")
    public PageDto<TransferDto> getTransfersPage(@PathVariable int accountId,
                                                 @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                 @RequestParam(name = "length", required = false, defaultValue = "100") int length,
                                                 @RequestParam(name = "sortField") Transfer.Fields sortField,
                                                 @RequestParam(name = "sortDirection") Sort.Direction direction) {
        return pageDtoMapper.toDto(transferService
                .findTransfersByAccountId(accountId, sortField, direction, page, length)
                .map(transfer -> {
                    Account payeeAccount = transfer.getAccount();
                    if (accountId == transfer.getAccount().getId()) {
                        payeeAccount = transfer.getSecondAccount();
                    }

                    return transferMapper.entityToDto(transfer, payeeAccount);
                }));
    }

    @PostMapping("/{accountId}/transfer/")
    public ResponseEntity<TransferDto> addTransfer(@RequestBody @Valid NewTransferDto transferDto,
                                                   @PathVariable(name = "accountId") int accountId)
            throws ResourceNotFoundException {

        if (accountId == transferDto.getToAccountId()) {
            throw new IllegalArgumentException("Destination account must be different from source account!");
        }

        Account fromAccount = accountService.findAccountById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("account", accountId));
        Account toAccount = accountService.findAccountById(transferDto.getToAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("account", transferDto.getToAccountId()));

        Transfer newTransfer = transferMapper.newDtoToEntity(transferDto, fromAccount, toAccount);
        Transfer savedEntity = transferService.saveTransferEntity(newTransfer);
        Account payeeAccount = savedEntity.getAccount();
        // Show account with ID different from {accountId} as payee
        if (savedEntity.getAccount().getId() == accountId) {
           payeeAccount = savedEntity.getSecondAccount();
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transferMapper.entityToDto(savedEntity, payeeAccount));
    }

}
