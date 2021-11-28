package com.mateuszkmita.thesis.core.interactor;

import com.mateuszkmita.thesis.core.exception.InvalidInputResourceException;
import com.mateuszkmita.thesis.core.service.AccountServiceInterface;
import com.mateuszkmita.thesis.core.service.PagedAndSortedTransactionServiceInterface;
import com.mateuszkmita.thesis.external.repository.TransactionsRepositoryInterface;
import com.mateuszkmita.thesis.model.Account;
import com.mateuszkmita.thesis.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionInteractor implements PagedAndSortedTransactionServiceInterface {

    private final TransactionsRepositoryInterface transactionsRepository;

    private final AccountServiceInterface accountService;

    @Override
    public Iterable<Transaction> findAllTransactions() {
        return transactionsRepository.findAll();
    }

    @Override
    public Optional<Transaction> findTransactionById(int id) {
        return transactionsRepository.findById(id);
    }

    @Override
    @Transactional
    public Transaction saveTransactionEntity(Transaction transaction) {
        if (transaction.getAccount() == null || transaction.getId() != null) {
            throw new InvalidInputResourceException("New transaction must have specified account and must not have an ID!");
        }

        Account account = transaction.getAccount();
        account.setBalance(account.getBalance() + transaction.getAmount());

        return transactionsRepository.save(transaction);
    }

    @Override
    @Transactional
    public Transaction updateTransactionEntity(Transaction oldTransaction, Transaction updatedTransaction) {
        if (updatedTransaction.getId() == null) {
            throw new InvalidInputResourceException("Updated transaction must not have null ID!");
        }

        Account account = updatedTransaction.getAccount();
        account.setBalance(account.getBalance() - oldTransaction.getAmount() + updatedTransaction.getAmount());

        return transactionsRepository.save(updatedTransaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(Transaction transaction) {
        Account account = transaction.getAccount();
        account.setBalance(account.getBalance() - transaction.getAmount());
        accountService.updateAccountEntity(account);

        transactionsRepository.delete(transaction);
    }

    @Override
    public Page<Transaction> findTransactionsByAccountId(int accountId, Transaction.Fields sortField,
                                                            Sort.Direction direction, int page, int length) {
        return transactionsRepository.
                findTransactionByAccount_Id(accountId, PageRequest.of(page, length).withSort(direction, sortField.name()));
    }
}
