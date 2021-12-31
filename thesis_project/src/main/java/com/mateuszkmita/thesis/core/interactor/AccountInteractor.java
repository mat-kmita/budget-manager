package com.mateuszkmita.thesis.core.interactor;

import com.mateuszkmita.thesis.core.exception.ResourceNotFoundException;
import com.mateuszkmita.thesis.core.service.AccountServiceInterface;
import com.mateuszkmita.thesis.core.service.CategoryServiceInterface;
import com.mateuszkmita.thesis.core.service.TransactionServiceInterface;
import com.mateuszkmita.thesis.external.repository.AccountRepositoryInterface;
import com.mateuszkmita.thesis.model.Account;
import com.mateuszkmita.thesis.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountInteractor implements AccountServiceInterface {

    private final AccountRepositoryInterface accountRepository;
    @Setter
    @Autowired
    private TransactionServiceInterface transactionService;
    private final CategoryServiceInterface categoryService;

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
        if (account.getBalance() > 0) {
            Transaction initialTransaction = new Transaction();
            initialTransaction.setAccount(account);
            initialTransaction.setAmount(account.getBalance());
            initialTransaction.setCategory(categoryService.getIncomesCategory());
            initialTransaction.setPayee("Initial transaction");
            initialTransaction.setDate(LocalDate.now());
            initialTransaction.setMemo("Initial balance of this account");

            account.setBalance(0);

            transactionService.saveTransactionEntity(initialTransaction);
        }
        return accountRepository.save(account);
    }

    @Override
    public Account updateAccountEntity(Account updatedAccount) {
        return accountRepository.save(updatedAccount);
    }

    @Override
    @Transactional
    public void deleteAccountById(int accountId) throws ResourceNotFoundException {
        Account account = findAccountById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("account", accountId));
        transactionService.deleteTransactionsByAccountId(account.getId());
        accountRepository.delete(account);
    }
}
