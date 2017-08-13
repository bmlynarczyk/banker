package it.introsoft.banker.model.raw;

import it.introsoft.banker.model.jpa.Transfer;
import it.introsoft.banker.service.type.MBankTransferTypeRecognizer;
import it.introsoft.banker.service.type.TransferTypeRecognizer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static it.introsoft.banker.model.raw.TransferRawUtils.mBankStringToMoneyValue;
import static it.introsoft.banker.model.raw.TransferRawUtils.toLocalDate;

@Slf4j
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class MBankTransferRaw implements TransferRaw {

    private static final TransferTypeRecognizer transferTypeRecognizer = new MBankTransferTypeRecognizer();
    private static final List<TransferType> TRANSFER_TYPES_WITH_MINUS_AMOUNT = newArrayList(
            TransferType.TAX_CHARGES,
            TransferType.INSURANCE_CHARGES,
            TransferType.BANK_CHARGES,
            TransferType.INTEREST_TAX_CHARGES,
            TransferType.CHARGES
    );
    private final String EMPTY = "";
    private String account;
    private String accounts;
    private String title;
    private String sender;
    private String transferType;
    private String date;
    private String referenceNumber;
    private String amount;
    private String symbol;

    @Override
    @SneakyThrows
    public Transfer asTransfer() {
        boolean isZus = sender != null && sender.contains("ZUS");
        boolean isTax = symbol != null;
        String description = getDescription(isZus, isTax);
        amount = amount.replaceAll("Kwota przelewu: ", EMPTY);
        amount = amount.replaceAll("Kwota w PLN: ", EMPTY);
        TransferType transferType = transferTypeRecognizer.recognize(getDescriber(isZus, isTax), amount);
        if (TRANSFER_TYPES_WITH_MINUS_AMOUNT.contains(transferType))
            amount = "-" + amount;
        return Transfer.builder()
                .account(account)
                .beneficiaryAccount(accounts.substring(accounts.lastIndexOf("Nr Rachunku: ")).replaceAll("Nr Rachunku: ", EMPTY))
                .date(toLocalDate(date.replaceAll("Data księgowania: ", EMPTY), "yyyy-MM-dd"))
                .amount(mBankStringToMoneyValue(amount))
                .description(description)
                .transferType(transferType)
                .currency("PLN")
                .bank(Bank.M_BANK)
                .dateTransferNumber(getDateTransferNumber())
                .build();
    }

    public long getDateTransferNumber() {
        referenceNumber = referenceNumber.replaceAll("Nr referencyjny operacji: ", EMPTY);
        referenceNumber = referenceNumber.replaceAll("Nr referencyjny: ", EMPTY);
        if (referenceNumber.contains("-"))
            referenceNumber = referenceNumber.split("-")[1];
        log.info(referenceNumber);
        referenceNumber = referenceNumber.replaceFirst("^0+(?!$)", EMPTY);
        log.info(referenceNumber);
        return Long.parseLong(referenceNumber);
    }

    private String getDescription(boolean isZus, boolean isTax) {
        if (isZus)
            return sender.substring(sender.indexOf("ZUS"));
        if (isTax)
            return symbol.replaceAll("Symbol: ", EMPTY);
        return title.replaceAll("Tytuł operacji: ", EMPTY);
    }

    private String getDescriber(boolean isZus, boolean isTax) {
        if (isZus)
            return "zus";
        if (isTax)
            return "us";
        return transferType.replaceAll("Rodzaj operacji: ", EMPTY);
    }

}
