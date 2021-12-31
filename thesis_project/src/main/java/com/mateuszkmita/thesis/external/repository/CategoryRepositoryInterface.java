package com.mateuszkmita.thesis.external.repository;

import com.mateuszkmita.thesis.model.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryRepositoryInterface extends CrudRepository<Category, Integer> {

    Optional<Category> findByName(String name);
    @Query("select c from Category c where c.id = 1")
    Optional<Category> findIncomesCategory();
}
