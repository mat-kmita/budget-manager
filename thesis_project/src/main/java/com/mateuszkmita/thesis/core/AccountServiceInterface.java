package com.mateuszkmita.thesis.core;

import com.mateuszkmita.thesis.core.exception.ResourceNotFoundException;
import com.mateuszkmita.thesis.model.Account;

import java.util.Optional;

public interface AccountServiceInterface {
    Iterable<Account> findAllAccounts();
    Optional<Account> findAccountById(int id);
    Account saveAccountEntity(Account account);
    Account updateAccountEntity(Account updatedAccount);
    void deleteAccountById(int accountId) throws ResourceNotFoundException;

}
