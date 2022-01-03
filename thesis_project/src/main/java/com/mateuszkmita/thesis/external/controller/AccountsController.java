package com.mateuszkmita.thesis.external.controller;

import com.mateuszkmita.thesis.core.exception.ResourceNotFoundException;
import com.mateuszkmita.thesis.core.service.AccountServiceInterface;
import com.mateuszkmita.thesis.core.service.CategoryServiceInterface;
import com.mateuszkmita.thesis.core.service.TransactionServiceInterface;
import com.mateuszkmita.thesis.core.service.TransferServiceInterface;
import com.mateuszkmita.thesis.external.controller.dto.account.AccountDto;
import com.mateuszkmita.thesis.external.controller.dto.account.AccountUpdateDto;
import com.mateuszkmita.thesis.external.controller.dto.account.NewAccountDto;
import com.mateuszkmita.thesis.external.controller.dto.page.PageDto;
import com.mateuszkmita.thesis.external.controller.dto.transaction.NewTransactionDto;
import com.mateuszkmita.thesis.external.controller.dto.transaction.TransactionDto;
import com.mateuszkmita.thesis.external.controller.dto.transfer.NewTransferDto;
import com.mateuszkmita.thesis.external.controller.dto.transfer.TransferDto;
import com.mateuszkmita.thesis.external.controller.dto.util.ProcedureResultDto;
import com.mateuszkmita.thesis.external.controller.mapper.AccountMapper;
import com.mateuszkmita.thesis.external.controller.mapper.PageDtoMapper;
import com.mateuszkmita.thesis.external.controller.mapper.TransactionMapper;
import com.mateuszkmita.thesis.external.controller.mapper.TransferMapper;
import com.mateuszkmita.thesis.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/api/v1/account/")
@CrossOrigin
public class AccountsController {

    private final AccountServiceInterface accountService;
    private final AccountMapper accountMapper;
    private final TransactionMapper transactionMapper;
    private final TransactionServiceInterface transactionService;
    private final PageDtoMapper pageDtoMapper;
    private final TransferMapper transferMapper;
    private final TransferServiceInterface transferService;
    private final CategoryServiceInterface categoryService;

    @GetMapping("/")
    public Iterable<AccountDto> getAllAccounts() {
        return StreamSupport
                .stream(accountService.findAllAccounts().spliterator(), false)
                .map(accountMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/")
    public ResponseEntity<AccountDto> createAccount(@RequestBody @Valid NewAccountDto requestDto) {
        Account account = accountMapper.toEntity(requestDto);
        Account savedAccount = accountService.saveAccountEntity(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountMapper.toDto(savedAccount));
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<Account> updateAccount(@PathVariable(name = "accountId") int accountId, @RequestBody AccountUpdateDto accountUpdateDto)
            throws ResourceNotFoundException {
        Optional<Account> existingAccount = accountService.findAccountById(accountId);

        if (existingAccount.isEmpty()) {
            throw new ResourceNotFoundException("account", accountId);
        }

        Account account = existingAccount.get();
        accountMapper.updateAccountEntity(accountUpdateDto, account);
        return ResponseEntity.status(HttpStatus.OK).body(accountService.updateAccountEntity(account));
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<ProcedureResultDto> deleteAccount(@PathVariable(name = "accountId") int accountId)
            throws ResourceNotFoundException {
        accountService.deleteAccountById(accountId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{accountId}/transaction/")
    @CrossOrigin(origins = "http://localhost:3000/")
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
    public ResponseEntity<TransactionDto> addTransaction(@RequestBody @Valid NewTransactionDto transactionDto,
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

        if (accountId == transferDto.toAccountId()) {
            throw new IllegalArgumentException("Destination account must be different from source account!");
        }

        Account fromAccount = accountService.findAccountById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("account", accountId));
        Account toAccount = accountService.findAccountById(transferDto.toAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("account", transferDto.toAccountId()));

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
