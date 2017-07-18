package it.introsoft.banker.service;

import it.introsoft.banker.repository.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
@ToString
public class UpdateAccountBalanceEvent {

    private final Account account;

}
