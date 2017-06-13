package it.introsoft.banker.repository;

import it.introsoft.banker.repository.validator.BankConstraint;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
    @Column(name = "BANK", nullable = false)
    String bank;
    @NotNull
    @Id
    private String number;
    @Column(name = "CURRENT_BALANCE", nullable = false)
    private Long currentBalance;
    private Boolean active;

}