package com.mateuszkmita.thesis.external.repository;

import com.mateuszkmita.thesis.model.Transfer;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TransfersRepositoryInterface extends PagingAndSortingRepository<Transfer, Integer> {
}
