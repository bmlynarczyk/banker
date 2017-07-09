package it.introsoft.banker.model.transfer.type;

import it.introsoft.banker.repository.Transfer;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static it.introsoft.banker.model.transfer.type.TransferType.*;

@Slf4j
public class PkoBpTransferTypeRecognizer {

    private Set<String> BANK_CHARGES_DESCRIPTIONS = newHashSet("Opłata", "Opłata za użytkowanie karty", "Obciążenie");

    private Set<String> CHARGES_DESCRIPTIONS = newHashSet("Wypłata gotówki w POS",
            "Przelew z rachunku", "Prowizja", "Wypłata gotówkowa z kasy");

    private Set<String> TAX_CHARGES_DESCRIPTIONS = newHashSet("Przelew do US", "Przelew podatkowy");

    private Set<String> DEPOSITS_DESCRIPTIONS = newHashSet("Uznanie", "Przelew na rachunek", "Zwrot płatności kartą",
            "Korekta", "Wpłata gotówkowa w kasie", "Przelew zagraniczny");

    public TransferType recognize(String describer, Transfer transfer) {
        if (null != transfer.getCardNumber() || "Płatność kartą".equals(describer))
            return CARD_PAYMENT;
        if ("Wypłata z bankomatu".equals(describer) || "Wypłata w bankomacie - kod mobilny".equals(describer))
            return ATM_WITHDRAWAL;
        if ("Zlecenie stałe".equals(describer))
            return STANDING_ORDER_CHARGES;
        if (null != transfer.getDescription() && transfer.getDescription().matches("000.{6} 74.{21}"))
            return UNKNOWN_CHARGES;
        if (BANK_CHARGES_DESCRIPTIONS.contains(describer))
            return BANK_CHARGES;
        if (CHARGES_DESCRIPTIONS.contains(describer))
            return CHARGES;
        if ("Podatek od odsetek".equals(describer))
            return INTEREST_TAX_CHARGES;
        if (TAX_CHARGES_DESCRIPTIONS.contains(describer))
            return TAX_CHARGES;
        if ("Naliczenie odsetek".equals(describer))
            return BANK_DEPOSIT;
        if (DEPOSITS_DESCRIPTIONS.contains(describer))
            return DEPOSIT;
        log.error("unknown transfer type. describer: $describer, amount: $amount");
        throw new IllegalStateException("unknown transfer type. describer: " + describer + ", amount: " + transfer.getAmount().toString());
    }

}
