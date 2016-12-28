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
    String beneficiaryAccount
    String bank
    String category
    List<String> tags
}