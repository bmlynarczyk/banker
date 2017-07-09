package it.introsoft.banker.repository;

import it.introsoft.banker.model.Bank;
import it.introsoft.banker.model.transfer.type.TransferType;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transfer")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Transfer {

    @Id
    @GeneratedValue
    private Long id;
    private Date date;
    private Long dateTransferNumber;
    private String description;
    private Long amount;
    private Long balance;
    private String currency;

    @Enumerated(EnumType.STRING)
    private TransferType transferType;

    private String account;
    private String beneficiaryName;
    private String beneficiaryAccount;
    private String beneficiaryAddress;

    @Enumerated(EnumType.STRING)
    private Bank bank;

    private String category;
    private String cardNumber;
    private String payeeName;
    private String payeeAccount;
    private String payeeAddress;

}
