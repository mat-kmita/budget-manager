package com.mateuszkmita.thesis.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "budgets_categories")
@NoArgsConstructor
@Getter
@Setter
public class BudgetCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "budget_id")
    private Budget budget;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    
    @Column(name = "spent")
    @Setter(AccessLevel.NONE)
    private int spent;

    @Column(name = "amount")
    private int amount;

    @Setter(AccessLevel.NONE)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "budgetCategory")
    private Set<Transaction> transactions;

//    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
//    @JoinColumn(name = "next_month_id")
//    private BudgetCategory nextMonthBudgetCategory;
//
//    @OneToOne(fetch = FetchType.LAZY, mappedBy = "nextMonthBudgetCategory")
//    private BudgetCategory previousMonthBudgetCategory;

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
        this.spent = transactions.stream().map(Transaction::getAmount).reduce(0, Integer::sum);
    }

    public void addTransaction(Transaction transaction) {
        if (this.transactions.add(transaction)) {
            this.spent += transaction.getAmount();
        }
    }

    public void removeTransaction(Transaction transaction) {
        if (this.transactions.remove(transaction)) {
            this.spent -= transaction.getAmount();
        }
    }

    public int available() {
        int previousMonthAvailable = Optional.ofNullable(this
                .getBudget()
                .getPreviousBudget())
                .map(Budget::getBudgetCategories)
                .orElse(List.of())
                .stream()
                .filter(x -> Objects.equals(x.getCategory().getId(), this.getCategory().getId()))
                .map(BudgetCategory::available)
                .findFirst()
                .orElse(0);

        if (previousMonthAvailable < 0) {
            previousMonthAvailable = 0;
        }

        return previousMonthAvailable + this.amount + this.getSpent();
    }

    public BudgetCategory(Integer id, Budget budget, Category category, int amount, Set<Transaction> transactions) {
        this.id = id;
        this.budget = budget;
        this.category = category;
        this.amount = amount;
        this.setTransactions(transactions);
    }
}
