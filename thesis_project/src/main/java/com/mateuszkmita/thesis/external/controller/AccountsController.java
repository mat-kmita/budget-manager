package com.mateuszkmita.thesis.external.controller;

import com.mateuszkmita.thesis.core.AccountServiceInterface;
import com.mateuszkmita.thesis.core.exception.ResourceNotFoundException;
import com.mateuszkmita.thesis.external.controller.dto.AccountUpdateDto;
import com.mateuszkmita.thesis.external.controller.dto.ProcedureResultDto;
import com.mateuszkmita.thesis.external.controller.mapper.AccountUpdateMapper;
import com.mateuszkmita.thesis.external.repository.AccountRepositoryInterface;
import com.mateuszkmita.thesis.model.Account;
import lombok.RequiredArgsConstructor;
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


    @GetMapping("/")
    public Iterable<Account> getAllAccounts() {
        return accountService.findAllAccounts();
    }

    @PostMapping("/")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.saveAccountEntity(account));
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<Account> updateAccount(@PathVariable(name = "accountId") int accountId, @RequestBody AccountUpdateDto accountUpdateDto) {
        Optional<Account> existingAccount = accountService.findAccountById(accountId);

        if(existingAccount.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Account account = existingAccount.get();
        accountUpdateMapper.updateAccountEntity(accountUpdateDto, account);
        return ResponseEntity.status(HttpStatus.OK).body(accountService.updateAccountEntity(account));
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<ProcedureResultDto> deleteAccount(@PathVariable(name = "accountId") int accountId) {
        try {
            accountService.deleteAccountById(accountId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ResourceNotFoundException e) {
            ProcedureResultDto procedureResultDto = new ProcedureResultDto();
            procedureResultDto.setMessage("Account with ID " + accountId + " not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(procedureResultDto);
        }
    }
}
