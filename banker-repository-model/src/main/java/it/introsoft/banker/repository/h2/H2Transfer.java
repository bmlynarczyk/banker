package it.introsoft.banker.repository.h2;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "transfer")
@Getter
@Setter
@ToString
public class H2Transfer {

    @Id
    @GeneratedValue
    private Long id;
    private Date date;
    private Long dateTransferNumber;
    private String description;
    private Long amount;
    private Long balance;
    private String currency;
    private String transferType;
    private String account;
    private String beneficiaryName;
    private String beneficiaryAccount;
    private String beneficiaryAddress;
    private String bank;
    private String category;
    private String cardNumber;
    private String payeeName;
    private String payeeAccount;
    private String payeeAddress;

}