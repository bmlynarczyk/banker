package it.introsoft.banker.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
@ToString
public class UpdateAccountEvent {

    private final String account;

}
