package com.mateuszkmita.thesis.external.repository;

import com.mateuszkmita.thesis.model.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryRepositoryInterface extends CrudRepository<Category, Integer> {
    Optional<Category> findByName(String name);
}
