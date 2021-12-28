package com.mateuszkmita.thesis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "budgets")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "month_year", nullable = false, updatable = false, unique = true)
    private LocalDate date;

    @OneToMany(mappedBy = "budget", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<BudgetCategory> budgetCategories;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "prev_budget_id")
    private Budget previousBudget;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "previousBudget")
    private Budget nextBudget;

    @Column(name = "overspent_sum")
    private int overspentCategoriesAmountSum;

    @Column(name = "incomes_sum")
    private int incomesSum;

    @Column(name = "budgeted_amount")
    private int budgetedAmount;

    public void updateBudgeted(int change) {
        this.setBudgetedAmount(this.getBudgetedAmount() + change);
    }

    public int available() {
        Budget previousBudget = this.getPreviousBudget();
        int overSpentAmount = 0;
        int previousBudgetAvailable = 0;

        if (previousBudget != null) {
            previousBudgetAvailable = previousBudget.available();
            overSpentAmount = previousBudget.getBudgetCategories()
                    .stream()
                    .filter(x -> x.getCategory().getId() != 1)
                    .map(BudgetCategory::available)
                    .filter(i -> i < 0)
                    .reduce(0, Integer::sum);
        }
        int budgetedAmount = this.getBudgetCategories()
                .stream()
                .filter(x -> x.getCategory().getId() != 1)
                .map(BudgetCategory::getAmount)
                .reduce(0, Integer::sum);
        int incomesSum = this.getBudgetCategories()
                .stream()
                .filter(x -> x.getCategory().getId() == 1)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Must have incomes category!"))
                .getSpent();

        return previousBudgetAvailable + incomesSum + overSpentAmount - budgetedAmount;
    }
}
