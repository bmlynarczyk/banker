package it.introsoft.banker.model.transfer.type

import groovy.util.logging.Slf4j

@Slf4j
class MilleniumTransferTypeRecognizer implements TransferTypeRecognizer {

    @Override
    TransferType recognize(String describer, String amount) {
        if( describer in ['TRANSAKCJA KARTĄ PŁATNICZĄ'])
            return TransferType.CHARGES
        log.error("unknown transfer type. describer: $describer, amount: $amount")
        throw new IllegalStateException("unknown transfer type. describer: $describer, amount: $amount")
    }
}