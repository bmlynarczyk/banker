package it.introsoft.banker.model.transfer.type;

import lombok.extern.slf4j.Slf4j;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

@Slf4j
public class MilleniumTransferTypeRecognizer implements TransferTypeRecognizer {

    private final Set<String> CHARGES_DESCRIPTIONS = newHashSet("TRANSAKCJA KARTĄ PŁATNICZĄ", "WYPŁATA KARTĄ Z BANKOMATU",
            "PRZELEW DO INNEGO BANKU", "PŁATNOŚĆ INTERNETOWA WYCHODZĄCA", "PRZELEW WEWNĘTRZNY WYCHODZĄCY");

    private final Set<String> DEPOSIT_DESCRIPTIONS = newHashSet("PRZELEW PRZYCHODZĄCY", "PRZELEW WEWNĘTRZNY PRZYCHODZĄCY");

    @Override
    public TransferType recognize(String describer, String amount) {
        if (CHARGES_DESCRIPTIONS.contains(describer))
            return TransferType.CHARGES;
        if (describer.equals("OPERACJE NA LOKATACH") && amount.startsWith("-"))
            return TransferType.CHARGES;
        if (DEPOSIT_DESCRIPTIONS.contains(describer))
            return TransferType.DEPOSIT;
        if (describer.equals("OPŁATA"))
            return TransferType.BANK_CHARGES;
        log.error("unknown transfer type. describer: " + describer + ", amount: " + amount);
        throw new IllegalStateException("unknown transfer type. describer: " + describer + ", amount: " + amount);
    }

}
