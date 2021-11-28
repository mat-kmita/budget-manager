package com.mateuszkmita.thesis.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "transfers")
@Getter
@Setter
public class Transfer {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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

}
