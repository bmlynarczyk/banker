package it.introsoft.banker.model.transfer.type

import groovy.transform.CompileStatic

@CompileStatic
enum TransferType {
    DEPOSIT,
    BANK_DEPOSIT,
    CHARGES,
    BANK_CHARGES,
    INTEREST_TAX_CHARGES,
    TAX_CHARGES,
    INSURANCE_CHARGES
}
