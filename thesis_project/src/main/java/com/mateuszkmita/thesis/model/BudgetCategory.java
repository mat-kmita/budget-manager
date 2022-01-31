package com.mateuszkmita.thesis.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "budgets_categories")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
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
    private int spent;

    @Column(name = "amount")
    private int amount;

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

    public BudgetCategory(Integer id, Budget budget, Category category, int amount) {
        this.id = id;
        this.budget = budget;
        this.category = category;
        this.amount = amount;
    }
}
