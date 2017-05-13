package it.introsoft.banker.model.transfer.type;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BgzOptimaTransferTypeRecognizer implements TransferTypeRecognizer {

    public TransferType recognize(String describer, String amount) {
        if (describer != null && describer.startsWith("ODSETKI"))
            return TransferType.BANK_DEPOSIT;
        if (describer != null && describer.startsWith("PODATEK"))
            return TransferType.INTEREST_TAX_CHARGES;
        if (describer != null && (describer.startsWith("PRZELEW") && amount.startsWith("-")))
            return TransferType.CHARGES;
        if (describer != null && (describer.startsWith("PRZELEW") || describer.startsWith("CHARGES/DEPOSIT")))
            return TransferType.DEPOSIT;
        log.error("unknown transfer type. describer: " + describer + ", amount: " + amount);
        throw new IllegalStateException("unknown transfer type. describer: " + describer + ", amount: " + amount);
    }

}
