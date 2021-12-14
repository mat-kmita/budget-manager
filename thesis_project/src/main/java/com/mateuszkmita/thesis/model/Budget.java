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

    @Column(name = "available", nullable = false)
    private int available;

    @OneToMany(mappedBy = "budget", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<BudgetCategory> budgetCategories;
}
