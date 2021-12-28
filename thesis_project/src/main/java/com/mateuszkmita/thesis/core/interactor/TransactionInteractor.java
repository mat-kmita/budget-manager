package com.mateuszkmita.thesis.core.interactor;

import com.mateuszkmita.thesis.core.service.*;
import com.mateuszkmita.thesis.external.repository.TransactionsRepositoryInterface;
import com.mateuszkmita.thesis.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionInteractor implements TransactionServiceInterface {

    private final TransactionsRepositoryInterface transactionsRepository;
    private final AccountServiceInterface accountService;
    private final BudgetServiceInterface budgetService;
    private final BudgetCategoryServiceInterface budgetCategoryService;
    private final CategoryServiceInterface categoryService;

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

        Budget budget = budgetService.findBudget(transaction.getDate().getMonthValue(), transaction.getDate().getYear())
                .orElseThrow(() -> new IllegalArgumentException("Nie można dodawać transakcji gdy nie ma budżetu!"));

        BudgetCategory budgetCategory = budget.getBudgetCategories().stream().filter(c -> c.getCategory().equals(transaction.getCategory())).findFirst().orElseThrow(() -> new RuntimeException("error"));
        budgetCategoryService.addTransaction(budgetCategory, transaction);

        transaction.setBudgetCategory(budgetCategory);

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

        BudgetCategory updatedBudgetCategory = budgetCategoryService.findBudgetCategoryByCategoryAndDate(
                updatedTransaction.getCategory(), updatedTransaction.getDate()).orElseThrow(() -> new IllegalArgumentException());
        if (!oldTransaction.getCategory().equals(updatedTransaction.getCategory())
                || oldTransaction.getDate().getMonthValue() != updatedTransaction.getDate().getMonthValue()
                || oldTransaction.getDate().getYear() != updatedTransaction.getDate().getYear()) {
            BudgetCategory oldBudgedCategory = budgetCategoryService.findBudgetCategoryByCategoryAndDate(
                    oldTransaction.getCategory(), oldTransaction.getDate()).orElseThrow(() -> new IllegalArgumentException());
            oldBudgedCategory.removeTransaction(oldTransaction);
            updatedBudgetCategory.addTransaction(updatedTransaction);
        }

        if (oldTransaction.getAmount() != updatedTransaction.getAmount()) {
            updatedBudgetCategory.removeTransaction(oldTransaction);
            updatedBudgetCategory.addTransaction(updatedTransaction);
        }

        return transactionsRepository.save(updatedTransaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(Transaction transaction) {
        Account account = transaction.getAccount();
        account.setBalance(account.getBalance() - transaction.getAmount());
        accountService.updateAccountEntity(account);

        transaction.getBudgetCategory().removeTransaction(transaction);

        transactionsRepository.delete(transaction);
    }

    @Override
    public int calculateIncomeByMonthAndYear(int monthValue, int year) {
        return transactionsRepository.findIncome(monthValue, year);
    }

    @Override
    public Iterable<Transaction> findByCategoryAndDate(Category category, LocalDate date) {
        return transactionsRepository.findByCategoryIdAndMonthYear(category.getId(), date.getMonthValue(), date.getYear());
    }

    @Override
    public Page<Transaction> findTransactionsByAccountId(int accountId, Transaction.Fields sortField,
                                                         Sort.Direction direction, int page, int length) {
        return transactionsRepository.
                findTransactionByAccount_Id(accountId, PageRequest.of(page, length).withSort(direction, sortField.name()));
    }
}
