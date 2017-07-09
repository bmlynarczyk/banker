package it.introsoft.banker.repository;

import it.introsoft.banker.model.Bank;
import it.introsoft.banker.repository.validator.BankConstraint;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @NotNull
    @BankConstraint
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Bank bank;
    @NotNull
    @Id
    private String number;
    @Column(name = "CURRENT_BALANCE")
    private Long currentBalance;
    private Boolean active;

}