package com.mateuszkmita.thesis.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="transactions")
@FieldNameConstants(asEnum = true)
@Getter
@Setter
public class Transaction {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    @Column(name = "memo")
    private String memo;

    @Column(name = "amount", nullable = false)
    private int amount;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false)
    private Account account;

    @Column(name = "payee", nullable = false)
    private String payee;

    @OneToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public Transaction copy() {
        Transaction existingTransactionCopy = new Transaction();
        existingTransactionCopy.setId(this.getId());
        existingTransactionCopy.setDate(this.getDate());
        existingTransactionCopy.setMemo(this.getMemo());
        existingTransactionCopy.setAmount(this.getAmount());
        existingTransactionCopy.setAccount(this.getAccount());
        existingTransactionCopy.setPayee(this.getPayee());
        existingTransactionCopy.setCategory(this.getCategory());

        return existingTransactionCopy;
    }
}
