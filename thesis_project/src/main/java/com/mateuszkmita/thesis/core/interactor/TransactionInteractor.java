package com.mateuszkmita.thesis.core.interactor;

import com.mateuszkmita.thesis.core.service.AccountServiceInterface;
import com.mateuszkmita.thesis.core.service.BudgetCategoryServiceInterface;
import com.mateuszkmita.thesis.core.service.BudgetServiceInterface;
import com.mateuszkmita.thesis.core.service.TransactionServiceInterface;
import com.mateuszkmita.thesis.external.repository.TransactionsRepositoryInterface;
import com.mateuszkmita.thesis.model.Account;
import com.mateuszkmita.thesis.model.Budget;
import com.mateuszkmita.thesis.model.BudgetCategory;
import com.mateuszkmita.thesis.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
                .orElseGet(() -> budgetService.createBudget(transaction.getDate().getMonthValue(), transaction.getDate().getYear()));

        BudgetCategory budgetCategory = budget.getBudgetCategories()
                .stream()
                .filter(c -> c.getCategory().equals(transaction.getCategory()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Did not find transaction category in this month's budget!"));
        budgetCategoryService.addTransaction(budgetCategory, transaction);

        return transactionsRepository.save(transaction);
    }

    @Override
    @Transactional
    public Transaction updateTransactionEntity(Transaction oldTransaction, Transaction updatedTransaction) {
        if (!Objects.equals(updatedTransaction.getId(), oldTransaction.getId())) {
            throw new IllegalArgumentException("Transaction update must not change the ID!");
        }

        if (!Objects.equals(updatedTransaction.getAccount().getId(), oldTransaction.getAccount().getId())) {
            throw new IllegalArgumentException("Transactions update must not change account!");
        }

        Account account = updatedTransaction.getAccount();
        account.setBalance(account.getBalance() - oldTransaction.getAmount() + updatedTransaction.getAmount());

        Budget budget = budgetService.findBudget(updatedTransaction.getDate().getMonthValue(), updatedTransaction.getDate().getYear())
                .orElseGet(() -> budgetService.createBudget(updatedTransaction.getDate().getMonthValue(), updatedTransaction.getDate().getYear()));

        BudgetCategory updatedBudgetCategory = budget.getBudgetCategories()
                .stream()
                .filter(c -> c.getCategory().equals(updatedTransaction.getCategory()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Did not find transaction category in this month's budget!"));

        if (!oldTransaction.getCategory().equals(updatedTransaction.getCategory())
                || oldTransaction.getDate().getMonthValue() != updatedTransaction.getDate().getMonthValue()
                || oldTransaction.getDate().getYear() != updatedTransaction.getDate().getYear()) {
            BudgetCategory oldBudgedCategory = budgetCategoryService
                    .findBudgetCategoryByCategoryAndDate(oldTransaction.getCategory(), oldTransaction.getDate())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found in budget!"));

            budgetCategoryService.removeTransaction(oldBudgedCategory, oldTransaction);
            budgetCategoryService.addTransaction(updatedBudgetCategory, updatedTransaction);
            return transactionsRepository.save(updatedTransaction);
        }

        if (oldTransaction.getAmount() != updatedTransaction.getAmount()) {
            budgetCategoryService.removeTransaction(updatedBudgetCategory, oldTransaction);
            budgetCategoryService.addTransaction(updatedBudgetCategory, updatedTransaction);
        }

        return transactionsRepository.save(updatedTransaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(Transaction transaction) {
        Account account = transaction.getAccount();
        account.setBalance(account.getBalance() - transaction.getAmount());
        accountService.updateAccountEntity(account);

        BudgetCategory budgetCategory = budgetCategoryService
                .findBudgetCategoryByCategoryAndDate(transaction.getCategory(), transaction.getDate())
                        .orElseThrow(() -> new IllegalArgumentException("Transaction not found in budget!"));
        budgetCategoryService.removeTransaction(budgetCategory, transaction);

        transactionsRepository.delete(transaction);
    }

    @Override
    public void deleteTransactionsByAccountId(int accountId) {
        List<Transaction> transactions = transactionsRepository.findAllByAccount_Id(accountId);
        transactions.forEach(this::deleteTransaction);
    }

    @Override
    public Page<Transaction> findTransactionsByAccountId(int accountId, Transaction.Fields sortField,
                                                         Sort.Direction direction, int page, int length) {
        return transactionsRepository.
                findTransactionByAccount_Id(accountId, PageRequest.of(page, length).withSort(direction, sortField.name()));
    }
}
