package com.mateuszkmita.thesis.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "transfers")
@FieldNameConstants(asEnum = true)
@Getter
@Setter
public class Transfer {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "memo")
    private String memo;

    @Column(name = "amount", nullable = false)
    private int amount;

    @OneToOne
    @JoinColumn(name = "from_account_id", referencedColumnName = "id", nullable = false)
    private Account account;

    @OneToOne
    @JoinColumn(name = "to_account_id", nullable = false, referencedColumnName = "id")
    private Account secondAccount;

    public Transfer copy() {
        Transfer transferCopy = new Transfer();
        transferCopy.setId(this.getId());
        transferCopy.setDate(this.getDate());
        transferCopy.setMemo(this.getMemo());
        transferCopy.setAmount(this.getAmount());
        transferCopy.setAccount(this.getAccount());
        transferCopy.setSecondAccount(this.getSecondAccount());

        return transferCopy;
    }
}
