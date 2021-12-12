package com.mateuszkmita.thesis.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "budgets_categories")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BudgetCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "budget_id")
    private Budget budget;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "balance")
    private int balance;

    @Column(name = "spent")
    private int spent;

    @Column(name = "amount")
    private int amount;
}
