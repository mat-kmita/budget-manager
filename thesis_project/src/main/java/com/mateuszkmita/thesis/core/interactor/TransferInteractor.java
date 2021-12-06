package com.mateuszkmita.thesis.core.interactor;

import com.mateuszkmita.thesis.core.service.TransferServiceInterface;
import com.mateuszkmita.thesis.external.repository.TransfersRepositoryInterface;
import com.mateuszkmita.thesis.model.Account;
import com.mateuszkmita.thesis.model.Transfer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TransferInteractor implements TransferServiceInterface {

    private final TransfersRepositoryInterface transfersRepository;

    @Override
    public Page<Transfer> findTransfersByAccountId(int accountId, Transfer.Fields sortField, Sort.Direction direction, int page, int length) {
        return transfersRepository.
                findTransferByAccountId(accountId, PageRequest.of(page, length).withSort(direction, sortField.name()));
    }

    @Override
    public Optional<Transfer> findTransferById(int id) {
        return transfersRepository.findById(id);
    }

    @Override
    @Transactional
    public Transfer saveTransferEntity(Transfer transfer) {
        if (transfer.getId() != null) {
            throw new IllegalArgumentException("New transfer must not have id!");
        }

        Account fromAccount = transfer.getAccount();
        Account toAccount = transfer.getSecondAccount();
        fromAccount.setBalance(fromAccount.getBalance() - transfer.getAmount());
        toAccount.setBalance(toAccount.getBalance() + transfer.getAmount());

        return transfersRepository.save(transfer);
    }

    @Override
    public Transfer updateTransferEntity(Transfer oldTransfer, Transfer updatedTransfer) {
        if (updatedTransfer.getId() == null || !Objects.equals(oldTransfer.getId(), updatedTransfer.getId())) {
            throw new IllegalArgumentException("Updated transfer must have not null ID!");
        }

        // Accounts have changed
        // Update accounts balance according to old transfer amount
        if (oldTransfer.getAccount().getId() != updatedTransfer.getAccount().getId()) {
            Account oldFromAccount = oldTransfer.getAccount();
            Account newFromAccount = updatedTransfer.getAccount();

            oldFromAccount.setBalance(oldFromAccount.getBalance() + oldTransfer.getAmount());
            newFromAccount.setBalance(newFromAccount.getBalance() - oldTransfer.getAmount());
        } else if (oldTransfer.getSecondAccount().getId() != updatedTransfer.getSecondAccount().getId()) {
            Account oldToAccount = oldTransfer.getSecondAccount();
            Account newToAccount = updatedTransfer.getSecondAccount();

            oldToAccount.setBalance(oldToAccount.getBalance() - oldTransfer.getAmount());
            newToAccount.setBalance(newToAccount.getBalance() + oldTransfer.getAmount());
        }

        // Amount has changed
        // Update current accounts' balance
        if (oldTransfer.getAmount() != updatedTransfer.getAmount()) {
            Account fromAccount = updatedTransfer.getAccount();
            Account toAccount = updatedTransfer.getAccount();

            fromAccount.setBalance(fromAccount.getBalance() + oldTransfer.getAmount() - updatedTransfer.getAmount());
            toAccount.setBalance(toAccount.getBalance() - oldTransfer.getAmount() + updatedTransfer.getAmount());
        }

        // Changes in other properties of a transfer don't require any actions beside saving new values in db
        return transfersRepository.save(updatedTransfer);
    }

    @Override
    public void deleteTransfer(Transfer transfer) {
        Account toAccount = transfer.getAccount();
        Account fromAccount = transfer.getSecondAccount();
        fromAccount.setBalance(fromAccount.getBalance() + transfer.getAmount());
        toAccount.setBalance(toAccount.getBalance() - transfer.getAmount());

        transfersRepository.delete(transfer);
    }
}
