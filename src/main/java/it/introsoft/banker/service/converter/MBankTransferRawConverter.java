package it.introsoft.banker.service.converter;

import it.introsoft.banker.model.raw.MBankTransferRaw;
import it.introsoft.banker.model.raw.TransferRaw;

import java.util.List;

public class MBankTransferRawConverter extends BaseTransferRawConverter {

    private final String account;

    public MBankTransferRawConverter(String account) {
        this.account = account;
    }

    @Override
    public TransferRaw convert(List<String> strings) {
        return MBankTransferRaw.builder()
                .account(account)
                .accounts(findLineWithPrefix("Nr Rachunku: ", strings))
                .title(findLineWithPrefix("Tytuł operacji: ", strings))
                .sender(findLineWithPrefix("Nadawca: ", strings))
                .symbol(findLineWithPrefix("Symbol: ", strings))
                .transferType(findLineWithPrefix("Rodzaj operacji: ", strings))
                .date(findLineWithPrefix("Data księgowania: ", strings))
                .referenceNumber(findLineWithPrefix("Nr referencyjny operacji: ", "Nr referencyjny: ", strings))
                .amount(getAmount(strings))
                .build();
    }

    private String getAmount(List<String> transferStrings) {
        String foundedAmount = findLineWithPrefix("Kwota przelewu: ", transferStrings);
        if (foundedAmount == null)
            return findLineWithPrefix("Kwota w PLN: ", transferStrings);
        return foundedAmount;
    }

}
