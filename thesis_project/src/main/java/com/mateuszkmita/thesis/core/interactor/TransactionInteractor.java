package com.mateuszkmita.thesis.core.interactor;

import com.mateuszkmita.thesis.core.service.AccountServiceInterface;
import com.mateuszkmita.thesis.core.service.TransactionServiceInterface;
import com.mateuszkmita.thesis.external.repository.TransactionsRepositoryInterface;
import com.mateuszkmita.thesis.model.Account;
import com.mateuszkmita.thesis.model.Category;
import com.mateuszkmita.thesis.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionInteractor implements TransactionServiceInterface {

    private final TransactionsRepositoryInterface transactionsRepository;
    private final AccountServiceInterface accountService;

    @Override
    public Optional<Transaction> findTransactionById(int id) {
        return transactionsRepository.findById(id);
    }

    @Override
    @Transactional
    public Transaction saveTransactionEntity(Transaction transaction) {
        if (transaction.getAccount() == null || transaction.getId() != null) {
            throw new IllegalArgumentException("New transaction must have specified account and must not have an ID!");
        }

        Account account = transaction.getAccount();
        account.setBalance(account.getBalance() + transaction.getAmount());

        

        return transactionsRepository.save(transaction);
    }

    @Override
    @Transactional
    public Transaction updateTransactionEntity(Transaction oldTransaction, Transaction updatedTransaction) {
        if (!Objects.equals(updatedTransaction.getId(), oldTransaction.getId())) {
            throw new IllegalArgumentException("Transaction update must not change the ID!");
        }

        if (updatedTransaction.getAccount().getId() != oldTransaction.getAccount().getId()) {
            throw new IllegalArgumentException("Transactions update must not change account!");
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
    public int calculateIncomeByMonthAndYear(int monthValue, int year) {
        return transactionsRepository.findIncome(monthValue, year);
    }

    @Override
    public int calculateAmountByCategoryAndDate(Category category, int month, int year) {
        return transactionsRepository.findAmount(category.getId(), month, year);
    }

    @Override
    public Page<Transaction> findTransactionsByAccountId(int accountId, Transaction.Fields sortField,
                                                            Sort.Direction direction, int page, int length) {
        return transactionsRepository.
                findTransactionByAccount_Id(accountId, PageRequest.of(page, length).withSort(direction, sortField.name()));
    }
}
