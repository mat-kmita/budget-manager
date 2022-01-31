package com.mateuszkmita.thesis.external.repository;

import com.mateuszkmita.thesis.model.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TransfersRepositoryInterface extends PagingAndSortingRepository<Transfer, Integer> {

    @Query("SELECT t FROM Transfer t WHERE account.id=?1 OR secondAccount.id=?1")
    Page<Transfer> findTransferByAccountId(int id, Pageable pageable);

    @Query("SELECT t FROM Transfer t WHERE account.id=?1 OR secondAccount.id=?1")
    List<Transfer> findTransferByAccountId(int id);
}
