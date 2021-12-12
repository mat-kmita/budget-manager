package com.mateuszkmita.thesis.external.repository;

import com.mateuszkmita.thesis.model.BudgetCategory;
import org.springframework.data.repository.CrudRepository;

public interface BudgetCategoryRepositoryInterface extends CrudRepository<BudgetCategory, Integer> {

}
