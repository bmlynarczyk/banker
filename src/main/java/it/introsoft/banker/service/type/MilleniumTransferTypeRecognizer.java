package it.introsoft.banker.service.type;

import it.introsoft.banker.model.raw.TransferType;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static it.introsoft.banker.model.raw.TransferType.*;

@Slf4j
public class MilleniumTransferTypeRecognizer implements TransferTypeRecognizer {

    private final Set<String> CHARGES_DESCRIPTIONS = newHashSet("PRZELEW DO INNEGO BANKU", "PŁATNOŚĆ INTERNETOWA WYCHODZĄCA", "PRZELEW WEWNĘTRZNY WYCHODZĄCY");

    private final Set<String> DEPOSIT_DESCRIPTIONS = newHashSet("PRZELEW PRZYCHODZĄCY", "PRZELEW WEWNĘTRZNY PRZYCHODZĄCY", "STAŁE ZLECENIE WEWNĄTRZ BANKU");

    @Override
    public TransferType recognize(String describer, String amount) {
        if ("STAŁE ZLECENIE ZEWNĘTRZNE".contains(describer))
            return STANDING_ORDER_CHARGES;
        if (CHARGES_DESCRIPTIONS.contains(describer))
            return TransferType.CHARGES;
        if ("TRANSAKCJA KARTĄ PŁATNICZĄ".equals(describer))
            return CARD_PAYMENT;
        if ("WYPŁATA KARTĄ Z BANKOMATU".equals(describer))
            return ATM_WITHDRAWAL;
        if (describer.equals("OPERACJE NA LOKATACH") && amount.startsWith("-"))
            return TransferType.CHARGES;
        if (DEPOSIT_DESCRIPTIONS.contains(describer))
            return TransferType.DEPOSIT;
        if (describer.equals("OPŁATA"))
            return TransferType.BANK_CHARGES;
        if (describer.equals("PRZELEW DO URZEDU SKARBOWEGO"))
            return TransferType.TAX_CHARGES;
        log.error("unknown transfer type. describer: " + describer + ", amount: " + amount);
        throw new IllegalStateException("unknown transfer type. describer: " + describer + ", amount: " + amount);
    }

}
