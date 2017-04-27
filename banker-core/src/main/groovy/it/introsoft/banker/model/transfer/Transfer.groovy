package it.introsoft.banker.model.transfer

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode
class Transfer {
    Date date
    String description
    Long amount
    Long balance
    Long dateTransferNumber
    String currency
    String transferType
    String account
    String beneficiaryName
    String beneficiaryAccount
    String beneficiaryAddress
    String beneficiaryBank
    String payeeName
    String payeeAccount
    String payeeAddress
    String payeeBank;
    String bank
    String category
    String cardNumber
    String cardName
    String cardOwner
    List<String> tags
}