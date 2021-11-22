package com.mateuszkmita.thesis.core;

import com.mateuszkmita.thesis.core.exception.ResourceNotFoundException;
import com.mateuszkmita.thesis.external.repository.AccountRepositoryInterface;
import com.mateuszkmita.thesis.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountInteractor implements AccountServiceInterface {

    private final AccountRepositoryInterface accountRepository;

    @Override
    public Iterable<Account> findAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> findAccountById(int id) {
        return accountRepository.findById(id);
    }

    @Override
    public Account saveAccountEntity(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account updateAccountEntity(Account updatedAccount) {
        return accountRepository.save(updatedAccount);
    }

    @Override
    public void deleteAccountById(int accountId) throws ResourceNotFoundException {
        Optional<Account> optionalAccount = findAccountById(accountId);

        accountRepository.delete(optionalAccount.orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono konta o ID" + accountId)));
    }
}
