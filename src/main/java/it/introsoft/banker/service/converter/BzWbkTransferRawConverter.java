package it.introsoft.banker.service.converter;

import it.introsoft.banker.model.raw.BzWbkTransferRaw;
import it.introsoft.banker.model.raw.TransferRaw;
import it.introsoft.banker.model.raw.TransferType;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static it.introsoft.banker.model.raw.TransferType.*;
import static it.introsoft.banker.service.converter.TransferTypeFunctionMatcher.of;
import static java.util.regex.Pattern.compile;

public class BzWbkTransferRawConverter implements Converter<String, TransferRaw> {

    private static final List<TransferTypeFunctionMatcher> matchers = newArrayList(
            of(DEPOSIT, compile(".*\\|.*\\|.*\\|.*\\|\\d{2}\\s\\d{4}\\s\\d{4}\\s\\d{4}\\s\\d{4}\\s\\d{4}\\s\\d{4}\\|\\d.*")::matcher),
            of(DEPOSIT, compile(".*ZWROT PŁATNOŚCI KARTĄ.*")::matcher),
            of(DEPOSIT, compile(".*PRZENIESIENIE SALDA.*")::matcher),
            of(CARD_PAYMENT, compile(".*V. SOL \\d{6}\\*{6}\\d{4} PŁATNOŚĆ KARTĄ.*")::matcher),
            of(ATM_WITHDRAWAL, compile(".*V. SOL \\d{6}\\*{6}\\d{4} WYPŁATA Z BANKOMATU KARTĄ.*")::matcher),
            of(CHARGES, compile(".*\\|.*\\|.*\\|.*\\|\\d{2}\\s\\d{4}\\s\\d{4}\\s\\d{4}\\s\\d{4}\\s\\d{4}\\s\\d{4}\\|\\-\\d.*")::matcher),
            of(CHARGES, compile(".*\\|WYPŁATA\\|.*")::matcher),
            of(BANK_CHARGES, compile(".*Opłata za prowadzenie rachunku.*")::matcher),
            of(BANK_CHARGES, compile(".*Opł\\. za przelew ELIXIR - BZWBK24.*")::matcher),
            of(BANK_CHARGES, compile(".*OPŁATA ZA WYPŁATĘ CASH BACK.*")::matcher),
            of(BANK_CHARGES, compile(".*Opłata za Przelew24.*")::matcher),
            of(BANK_CHARGES, compile(".*Opłata za przelew BlueCash.*")::matcher),
            of(BANK_CHARGES, compile(".*za wypłatę z bankomatu .*")::matcher),
            of(BANK_CHARGES, compile(".*Sprawdzenie dostępnych środków.*")::matcher),
            of(BANK_CHARGES, compile(".*Pobrane odsetki od salda ujemn.*")::matcher),
            of(BANK_CHARGES, compile(".*Opłata miesięczna za kartę.*")::matcher),
            of(BANK_CHARGES, compile(".*WYPŁATA Z BANKOMATU KARTĄ.*\\|\\-5\\,00\\|.*")::matcher)
    );

    private final String account;

    public BzWbkTransferRawConverter(String account) {
        this.account = account;
    }

    @Override
    public TransferRaw convert(String strings) {
        return matchers.stream()
                .filter(element -> element.getMatcherFunction().apply(strings).matches())
                .findFirst()
                .map(element -> raw(strings, element.getTransferType()))
                .orElseThrow(() -> new IllegalArgumentException("bz wbk unknown csv line format"));
    }

    private TransferRaw raw(String line, TransferType transferType) {
        String[] values = line.split("\\|", -1);
        return BzWbkTransferRaw.builder()
                .transferType(transferType)
                .accountingDate(values[0])
                .operationDate(values[1])
                .title(values[2])
                .beneficiaryName(values[3])
                .beneficiaryAccount(values[4])
                .amount(values[5])
                .balance(values[6])
                .account(account)
                .build();
    }

}
