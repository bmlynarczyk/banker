package it.introsoft.banker.model.transfer

import groovy.transform.ToString
import org.springframework.data.annotation.Id

@ToString
class Transfer {
    Date date
    String description
    Long amount
    Long balance
    String currency
    String transferType
    String account
    String beneficiaryAccount
    String bank
    String category
    List<String> tags
}