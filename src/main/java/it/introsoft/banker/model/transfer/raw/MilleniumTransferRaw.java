package it.introsoft.banker.model.transfer.raw;

import it.introsoft.banker.model.Bank;
import it.introsoft.banker.model.transfer.Transfer;
import it.introsoft.banker.model.transfer.supplier.MoneyConverter;
import it.introsoft.banker.model.transfer.type.TransferType;
import it.introsoft.banker.model.transfer.type.TransferTypeRecognizer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;


@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class MilleniumTransferRaw implements TransferRaw {

    private static final TransferTypeRecognizer transferTypeRecognizer = Bank.MILLENIUM.typeRecognizer();

    private String account;
    private String accountedAmount;
    private String accountingDate;
    private String transferDate;
    private String beneficiaryAccount;
    private String beneficiaryBank;
    private String beneficiaryName;
    private String cardName;
    private String cardNumber;
    private String cardOwner;
    private String date;
    private String dateTransferNumber;
    private String payeeAccount;
    private String payeeBank;
    private String payeeName;
    private String title;
    private String transferType;

    private static String replace(String replacement, String text) {
        return text != null ? text.replaceAll(replacement, "") : null;
    }

    @Override
    @SneakyThrows
    public Transfer asTransfer() {
        Bank bank = Bank.MILLENIUM;
        this.transferType = this.transferType.replaceAll("Typ operacji ", "");
        String amount = accountedAmount.replaceAll("Kwota zaksięgowana ", "");
        TransferType transferType = transferTypeRecognizer.recognize(this.transferType, amount);
        return Transfer.builder()
                .account(replace("Z rachunku ", account))
                .amount(MoneyConverter.milleniumStringToMoneyValue(amount))
                .bank(bank.getName())
                .beneficiaryAccount(replace("Na rachunek ", beneficiaryAccount))
                .beneficiaryName(replace("Odbiorca ", beneficiaryName))
                .beneficiaryBank(replace("Bank odbiorcy ", beneficiaryBank))
                .cardNumber(replace("Numer karty ", cardNumber))
                .cardName(replace("Karta ", cardName))
                .cardOwner(replace("Posiadacz karty ", cardOwner))
                .currency("PLN")
                .date(new SimpleDateFormat("yyyy-MM-dd").parse(date.replaceAll("Data księgowania ", "")))
                .dateTransferNumber(Long.parseLong(dateTransferNumber.replaceAll("Dzienny numer transakcji ", "")))
                .description(replace("Tytuł ", title))
                .payeeAccount(replace("Z rachunku ", payeeAccount))
                .payeeName(replace("Zleceniodawca ", payeeName))
                .payeeBank(payeeBank)
                .transferType(transferType.name())
                .build();
    }

}