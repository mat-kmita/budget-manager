package com.mateuszkmita.thesis.core.service;

import com.mateuszkmita.thesis.model.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public interface TransferServiceInterface {

    Page<Transfer> findTransfersByAccountId(int accountId, Transfer.Fields sortField,
                                                  Sort.Direction direction, int page, int length);

    Optional<Transfer> findTransferById(int id);

    Transfer saveTransferEntity(Transfer transfer);

    Transfer updateTransferEntity(Transfer oldTransfer, Transfer updatedTransfer);

    void deleteTransfer(Transfer transfer);
}
