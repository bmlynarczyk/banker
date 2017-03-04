package it.introsoft.banker.model.transfer.type

import groovy.util.logging.Slf4j

@Slf4j
class MBankTransferTypeRecognizer implements TransferTypeRecognizer {

    @Override
    TransferType recognize(String describer, String amount) {
        if (describer == 'zus')
            return TransferType.INSURANCE_CHARGES
        if (describer == 'us')
            return TransferType.TAX_CHARGES
        if (describer in ['PRZELEW ZEWNĘTRZNY PRZYCHODZĄCY', 'PRZELEW WEWNĘTRZNY PRZYCHODZĄCY', 'PRZELEW EXPRESSOWY PRZELEW PRZYCH.'])
            return TransferType.DEPOSIT
        if (describer in ['PRZELEW ZEWNĘTRZNY WYCHODZĄCY', 'PRZELEW MTRANSFER WYCHODZACY'])
            return TransferType.CHARGES
        log.error("unknown transfer type. describer: $describer, amount: $amount")
        throw new IllegalStateException("unknown transfer type. describer: $describer, amount: $amount")
    }

}
