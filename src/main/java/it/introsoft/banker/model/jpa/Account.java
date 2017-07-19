package it.introsoft.banker.model.jpa;

import it.introsoft.banker.model.jpa.validator.BankConstraint;
import it.introsoft.banker.model.raw.Bank;
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