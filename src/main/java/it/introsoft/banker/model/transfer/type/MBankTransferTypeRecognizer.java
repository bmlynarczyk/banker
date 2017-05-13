package it.introsoft.banker.model.transfer.type;

import lombok.extern.slf4j.Slf4j;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

@Slf4j
public class MBankTransferTypeRecognizer implements TransferTypeRecognizer {

    private final Set<String> DEPOSIT_DESCRIPTIONS = newHashSet("PRZELEW ZEWNĘTRZNY PRZYCHODZĄCY", "PRZELEW WEWNĘTRZNY PRZYCHODZĄCY",
            "PRZELEW EXPRESSOWY PRZELEW PRZYCH.");

    private final Set<String> CHARGES_DESCRIPTIONS = newHashSet("PRZELEW ZEWNĘTRZNY WYCHODZĄCY", "PRZELEW MTRANSFER WYCHODZACY");

    @Override
    public TransferType recognize(String describer, String amount) {
        if (describer.equals("zus"))
            return TransferType.INSURANCE_CHARGES;
        if (describer.equals("us"))
            return TransferType.TAX_CHARGES;
        if (DEPOSIT_DESCRIPTIONS.contains(describer))
            return TransferType.DEPOSIT;
        if (CHARGES_DESCRIPTIONS.contains(describer))
            return TransferType.CHARGES;
        log.error("unknown transfer type. describer: " + describer + ", amount: " + amount);
        throw new IllegalStateException("unknown transfer type. describer: " + describer + ", amount: " + amount);
    }

}
