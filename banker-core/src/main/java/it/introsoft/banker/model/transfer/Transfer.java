package it.introsoft.banker.model.transfer;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Date;
import java.util.List;

@Value
@Builder
@EqualsAndHashCode
public class Transfer {
    Date date;
    String description;
    Long amount;
    Long balance;
    Long dateTransferNumber;
    String currency;
    String transferType;
    String account;
    String beneficiaryName;
    String beneficiaryAccount;
    String beneficiaryAddress;
    String beneficiaryBank;
    String payeeName;
    String payeeAccount;
    String payeeAddress;
    String payeeBank;
    String bank;
    String category;
    String cardNumber;
    String cardName;
    String cardOwner;
    List<String> tags;
}