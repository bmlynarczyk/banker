package it.introsoft.banker.model.raw;

import it.introsoft.banker.model.jpa.Transfer;
import it.introsoft.banker.service.type.MilleniumTransferTypeRecognizer;
import it.introsoft.banker.service.type.TransferTypeRecognizer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;


@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class MilleniumTransferRaw implements TransferRaw {

    private static final TransferTypeRecognizer transferTypeRecognizer = new MilleniumTransferTypeRecognizer();

    private String account;
    private String accountedAmount;
    private String amount;
    private String accountingDate;
    private String transferDate;
    private String beneficiaryAccount;
    private String beneficiaryName;
    private String cardName;
    private String cardNumber;
    private String cardOwner;
    private String date;
    private String dateTransferNumber;
    private String payeeAccount;
    private String payeeName;
    private String title;
    private String additionalDescription;
    private String transferType;

    private static String replace(String replacement, String text) {
        return text != null ? text.replaceAll(replacement, "") : null;
    }

    @Override
    @SneakyThrows
    public Transfer asTransfer() {
        this.transferType = this.transferType.replaceAll("Typ operacji ", "");
        String amount;
        if (accountedAmount != null)
            amount = accountedAmount.replaceAll("Kwota zaksięgowana ", "");
        else
            amount = this.amount.replaceAll("Kwota ", "");
        if (title != null)
            title = replace("Tytuł ", title);
        else
            title = replace("Dodatkowy opis ", additionalDescription);
        TransferType transferType = transferTypeRecognizer.recognize(this.transferType, amount);
        return Transfer.builder()
                .account(replace("Z rachunku ", account))
                .amount(MoneyConverter.milleniumStringToMoneyValue(amount))
                .bank(Bank.MILLENIUM)
                .beneficiaryAccount(replace("Na rachunek ", beneficiaryAccount))
                .beneficiaryName(replace("Odbiorca ", beneficiaryName))
                .cardNumber(replace("Numer karty ", cardNumber))
                .currency("PLN")
                .date(new SimpleDateFormat("yyyy-MM-dd").parse(date.replaceAll("Data księgowania ", "")))
                .dateTransferNumber(Long.parseLong(dateTransferNumber.replaceAll("Dzienny numer transakcji ", "")))
                .description(title)
                .payeeAccount(replace("Z rachunku ", payeeAccount))
                .payeeName(replace("Zleceniodawca ", payeeName))
                .transferType(transferType)
                .build();
    }

}
