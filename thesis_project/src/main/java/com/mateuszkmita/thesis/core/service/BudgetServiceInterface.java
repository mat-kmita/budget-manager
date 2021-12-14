package com.mateuszkmita.thesis.core.service;

import com.mateuszkmita.thesis.core.exception.ResourceNotFoundException;
import com.mateuszkmita.thesis.model.Budget;

import java.util.Optional;

public interface BudgetServiceInterface {
    Optional<Budget> findBudget(int month, int year);
    Optional<Budget> findBudget(int id);
    Budget createBudget(int month, int year);
    int calculateAmountAvailable(int budgetId) throws ResourceNotFoundException;
}
