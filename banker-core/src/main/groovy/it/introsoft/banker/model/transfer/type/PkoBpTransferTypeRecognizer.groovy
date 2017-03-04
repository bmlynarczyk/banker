package it.introsoft.banker.model.transfer.type

import groovy.util.logging.Slf4j

import static it.introsoft.banker.model.transfer.type.TransferType.*

@Slf4j
class PkoBpTransferTypeRecognizer implements TransferTypeRecognizer {

    private List<String> CHARGES_DESCRIPTIONS = ['Zlecenie stałe', 'Wypłata gotówki w POS',
                                                 'Wypłata w bankomacie - kod mobilny', 'Przelew z rachunku',
                                                 'Płatność kartą', 'Wypłata z bankomatu', 'Prowizja',
                                                 'Wypłata gotówkowa z kasy']

    private List<String> DEPOSITS_DESCRIPTIONS = ['Uznanie', 'Przelew na rachunek', 'Zwrot płatności kartą',
                                                  'Korekta', 'Wpłata gotówkowa w kasie', 'Przelew zagraniczny']

    @Override
    TransferType recognize(String describer, String amount) {
        if (describer in ['Opłata', 'Opłata za użytkowanie karty', 'Obciążenie'])
            return BANK_CHARGES
        if (describer in CHARGES_DESCRIPTIONS)
            return CHARGES
        if (describer == 'Podatek od odsetek')
            return INTEREST_TAX_CHARGES
        if (describer in ['Przelew do US', 'Przelew podatkowy'])
            return TAX_CHARGES
        if (describer in ['Naliczenie odsetek'])
            return BANK_DEPOSIT
        if (describer in DEPOSITS_DESCRIPTIONS)
            return DEPOSIT
        log.error("unknown transfer type. describer: $describer, amount: $amount")
        throw new IllegalStateException("unknown transfer type. describer: $describer, amount: $amount")
    }

}
