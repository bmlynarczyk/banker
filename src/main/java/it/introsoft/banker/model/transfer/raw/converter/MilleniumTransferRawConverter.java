package it.introsoft.banker.model.transfer.raw.converter;

import it.introsoft.banker.model.transfer.raw.MilleniumTransferRaw;
import it.introsoft.banker.model.transfer.raw.TransferRaw;

import java.util.List;

public class MilleniumTransferRawConverter extends BaseTransferRawConverter {

    private String account;

    public MilleniumTransferRawConverter(String account) {
        this.account = account;
    }

    @Override
    public TransferRaw convert(List<String> strings) {
        return MilleniumTransferRaw.builder()
                .transferType(findLineWithPrefix("Typ operacji ", strings))
                .dateTransferNumber(findLineWithPrefix("Dzienny numer transakcji ", strings))
                .date(findLineWithPrefix("Data księgowania ", strings))
                .accountingDate(findLineWithPrefix("Data waluty ", strings))
                .transferDate(findLineWithPrefix("Data transakcji ", strings))
                .payeeAccount(findLineWithPrefix("Z rachunku ", strings))
                .payeeName(findLineWithPrefix("Zleceniodawca ", strings))
                .cardName(findLineWithPrefix("Karta ", strings))
                .cardNumber(findLineWithPrefix("Numer karty ", strings))
                .cardOwner(findLineWithPrefix("Posiadacz karty ", strings))
                .beneficiaryAccount(findLineWithPrefix("Na rachunek ", strings))
                .beneficiaryName(findLineWithPrefix("Odbiorca ", strings))
                .title(findLineWithPrefix("Tytuł ", strings))
                .accountedAmount(findLineWithPrefix("Kwota zaksięgowana ", strings))
                .amount(findLineWithPrefix("Kwota ", strings))
                .account(account)
                .build();
    }

}
