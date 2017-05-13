package it.introsoft.banker.model.transfer.type;

import lombok.extern.slf4j.Slf4j;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static it.introsoft.banker.model.transfer.type.TransferType.*;

@Slf4j
public class PkoBpTransferTypeRecognizer implements TransferTypeRecognizer {

    private Set<String> BANK_CHARGES_DESCRIPTIONS = newHashSet("Opłata", "Opłata za użytkowanie karty", "Obciążenie");

    private Set<String> CHARGES_DESCRIPTIONS = newHashSet("Zlecenie stałe", "Wypłata gotówki w POS",
            "Wypłata w bankomacie - kod mobilny", "Przelew z rachunku", "Płatność kartą", "Wypłata z bankomatu",
            "Prowizja", "Wypłata gotówkowa z kasy");

    private Set<String> TAX_CHARGES_DESCRIPTIONS = newHashSet("Przelew do US", "Przelew podatkowy");

    private Set<String> DEPOSITS_DESCRIPTIONS = newHashSet("Uznanie", "Przelew na rachunek", "Zwrot płatności kartą",
            "Korekta", "Wpłata gotówkowa w kasie", "Przelew zagraniczny");

    @Override
    public TransferType recognize(String describer, String amount) {
        if (BANK_CHARGES_DESCRIPTIONS.contains(describer))
            return BANK_CHARGES;
        if (CHARGES_DESCRIPTIONS.contains(describer))
            return CHARGES;
        if (describer.equals("Podatek od odsetek"))
            return INTEREST_TAX_CHARGES;
        if (TAX_CHARGES_DESCRIPTIONS.contains(describer))
            return TAX_CHARGES;
        if (describer.equals("Naliczenie odsetek"))
            return BANK_DEPOSIT;
        if (DEPOSITS_DESCRIPTIONS.contains(describer))
            return DEPOSIT;
        log.error("unknown transfer type. describer: $describer, amount: $amount");
        throw new IllegalStateException("unknown transfer type. describer: " + describer + ", amount: " + amount);
    }

}
