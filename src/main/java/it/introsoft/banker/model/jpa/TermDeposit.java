package it.introsoft.banker.model.jpa;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TermDeposit {

    @Id
    @GeneratedValue
    private Long id;

    private String account;

    private String bank;

    private String name;

    private String interest;

    private LocalDate openingDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opening_transfer_id", nullable = false, foreignKey = @ForeignKey(name = "opening_transfer_fkey"))
    private Transfer openingTransfer;

    private Long income;

    private Long amount;

    private LocalDate closingDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "closing_transfer_id", foreignKey = @ForeignKey(name = "closing_transfer_fkey"))
    private Transfer closingTransfer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "income_transfer_id", foreignKey = @ForeignKey(name = "income_transfer_fkey"))
    private Transfer incomeTransfer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TermDepositState state;

}
