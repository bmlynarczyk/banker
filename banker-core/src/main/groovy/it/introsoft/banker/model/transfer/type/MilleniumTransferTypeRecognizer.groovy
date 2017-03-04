package it.introsoft.banker.model.transfer.type

import groovy.util.logging.Slf4j

@Slf4j
class MilleniumTransferTypeRecognizer implements TransferTypeRecognizer {

    @Override
    TransferType recognize(String describer, String amount) {
        if (describer in ['TRANSAKCJA KARTĄ PŁATNICZĄ', 'WYPŁATA KARTĄ Z BANKOMATU', 'PRZELEW DO INNEGO BANKU',
                          'PŁATNOŚĆ INTERNETOWA WYCHODZĄCA', 'PRZELEW WEWNĘTRZNY WYCHODZĄCY'])
            return TransferType.CHARGES
        if (describer in ['OPERACJE NA LOKATACH'] && amount.startsWith('-'))
            return TransferType.CHARGES
        if (describer in ['PRZELEW PRZYCHODZĄCY', 'PRZELEW WEWNĘTRZNY PRZYCHODZĄCY'])
            return TransferType.DEPOSIT
        if (describer in ['OPŁATA'])
            return TransferType.BANK_CHARGES
        log.error("unknown transfer type. describer: $describer, amount: $amount")
        throw new IllegalStateException("unknown transfer type. describer: $describer, amount: $amount")
    }
}