package it.introsoft.banker.model.transfer.type

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@Slf4j
@CompileStatic
class BgzOptimaTransferTypeRecognizer implements TransferTypeRecognizer {

    TransferType recognize(String describer, String amount) {
        if (describer?.startsWith('ODSETKI'))
            return TransferType.BANK_DEPOSIT
        if (describer?.startsWith('PODATEK'))
            return TransferType.INTEREST_TAX_CHARGES
        if (describer?.startsWith('PRZELEW') && amount.startsWith('-'))
            return TransferType.CHARGES
        if (describer?.startsWith('PRZELEW') || describer?.startsWith('CHARGES/DEPOSIT'))
            return TransferType.DEPOSIT
        log.error("unknown transfer type. describer: $describer, amount: $amount")
        throw new IllegalStateException("unknown transfer type. describer: $describer, amount: $amount")
    }

}
