package it.introsoft.banker.model.transfer.type

import groovy.util.logging.Slf4j

import static TransferType.BANK_CHARGES
import static TransferType.BANK_DEPOSIT
import static TransferType.CHARGES
import static TransferType.DEPOSIT
import static TransferType.INTEREST_TAX_CHARGES
import static TransferType.TAX_CHARGES

@Slf4j
class PkoBpTransferTypeRecognizer implements TransferTypeRecognizer {

    @Override
    TransferType recognize(String describer, String amount) {
        if(describer in ['Opłata', 'Opłata za użytkowanie karty', 'Obciążenie'])
            return BANK_CHARGES
        if(describer in ['Zlecenie stałe', 'Wypłata gotówki w POS', 'Wypłata w bankomacie - kod mobilny', 'Przelew z rachunku', 'Płatność kartą', 'Wypłata z bankomatu', 'Prowizja', 'Wypłata gotówkowa z kasy'])
            return CHARGES
        if(describer == 'Podatek od odsetek')
            return INTEREST_TAX_CHARGES
        if(describer in ['Przelew do US', 'Przelew podatkowy'])
            return TAX_CHARGES
        if(describer in ['Naliczenie odsetek'])
            return BANK_DEPOSIT
        if(describer in ['Uznanie', 'Przelew na rachunek', 'Zwrot płatności kartą', 'Korekta', 'Wpłata gotówkowa w kasie'])
            return DEPOSIT
        if(describer == 'Przelew zagraniczny' && amount.startsWith('+'))
            return DEPOSIT
        log.error("unknown transfer type. describer: $describer, amount: $amount")
        throw new IllegalStateException("unknown transfer type. describer: $describer, amount: $amount")
    }

}
