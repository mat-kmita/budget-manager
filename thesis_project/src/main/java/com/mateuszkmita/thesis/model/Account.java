package com.mateuszkmita.thesis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
@Table(name = "accounts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "name")
    @Length(min = 3, max = 50)
    private String name;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name = "description")
    @Length(max = 100)
    private String description;

    @Column(name = "balance")
    private int balance;
}
