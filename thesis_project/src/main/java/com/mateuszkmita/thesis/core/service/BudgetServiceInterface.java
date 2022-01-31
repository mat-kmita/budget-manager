package com.mateuszkmita.thesis.core.service;

import com.mateuszkmita.thesis.model.Budget;

import java.util.Optional;
import java.util.stream.Stream;

public interface BudgetServiceInterface {
    Stream<Budget> findAllSortedByDate();
    Optional<Budget> findBudget(int month, int year);
    Optional<Budget> findBudget(int id);
    Budget createBudget(int month, int year);
}
