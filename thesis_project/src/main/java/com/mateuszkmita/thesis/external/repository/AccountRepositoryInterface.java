package com.mateuszkmita.thesis.external.repository;

import com.mateuszkmita.thesis.model.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepositoryInterface extends CrudRepository<Account, Integer> {
}
