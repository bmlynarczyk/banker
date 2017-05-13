package it.introsoft.banker.model.transfer.raw;

import it.introsoft.banker.model.Bank;
import it.introsoft.banker.model.transfer.Transfer;
import it.introsoft.banker.model.transfer.supplier.MoneyConverter;
import it.introsoft.banker.model.transfer.type.TransferTypeRecognizer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
@Builder
public class PkoBpTransferRaw implements TransferRaw {

    private static final Pattern outgoingPayment = Pattern.compile("(Rachunek odbiorcy : (.*))(Nazwa odbiorcy : (.*))(Adres odbiorcy : (.*))(Tytuł : (.*))");
    private static final Pattern mobileOutgoingPayment = Pattern.compile("(Numer telefonu : (.*))(Lokalizacja : (.*))(Data i czas operacji : (.*))(Bankomat : (.*))(Numer referencyjny : (.*))");
    private static final Pattern outgoingPaymentWithoutAddress = Pattern.compile("(Rachunek odbiorcy : (.*))(Nazwa odbiorcy : (.*))(Tytuł : (.*))");
    private static final Pattern outgoingPaymentWithoutAccount = Pattern.compile("(Nazwa odbiorcy : (.*))(Adres odbiorcy : (.*))(Tytuł : (.*))");
    private static final Pattern outgoingPaymentWithoutTitle = Pattern.compile("(Rachunek odbiorcy : (.*))(Nazwa odbiorcy : (.*))(Adres odbiorcy : (.*))");
    private static final Pattern canceledOutgoingPayment = Pattern.compile("PRZELEW WYCHODZĄCY ANUL.");
    private static final Pattern cardPayment = Pattern.compile("(Tytuł : (.*))(Lokalizacja : (.*))(Data i czas operacji : (.*))(Oryginalna kwota operacji : (.*))(Numer karty : (.*))");
    private static final Pattern incomingPayment = Pattern.compile("(Rachunek nadawcy : (.*))(Nazwa nadawcy : (.*))(Adres nadawcy : (.*))(Tytuł : (.*))");
    private static final Pattern incomingPaymentWithoutAccount = Pattern.compile("(Nazwa nadawcy : (.*))(Adres nadawcy : (.*))(Tytuł : (.*))");
    private static final Pattern incomingPaymentWithoutName = Pattern.compile("(Rachunek nadawcy : (.*))(Adres nadawcy : (.*))(Tytuł : (.*))");
    private static final Pattern incomingPaymentWithoutTitle = Pattern.compile("(Rachunek nadawcy : (.*))(Nazwa nadawcy : (.*))(Adres nadawcy : (.*))");
    private static final Pattern incomingPaymentWithoutAddress = Pattern.compile("(Rachunek nadawcy : (.*))(Nazwa nadawcy : (.*))(Tytuł : (.*))");
    private static final Pattern foreignPayment = Pattern.compile("(Rachunek nadawcy : (.*))(Nazwa nadawcy : (.*))(Tytuł : (.*))(Kwota w walucie oryginalnej : (.*))(Kurs przewalutowania : (.*))(Instrukcja kosztowa : (.*))(Referencje polecenia wypłaty : (.*))");
    private static final Pattern onlyTitle = Pattern.compile("(Tytuł : (.*))");
    private static final TransferTypeRecognizer transferTypeRecognizer = Bank.PKO_BP.typeRecognizer();
    private String account;
    private String description;
    private String transferType;
    private String date;
    private String amount;
    private String currency;
    private String balance;

    private static String getMoneyString(String value) {
        return value.replaceAll("\\+", "").replaceAll("\\.", ",");
    }

    @Override
    @SneakyThrows
    public Transfer asTransfer() {
        TransferDetails details = new TransferDetails(description);
        return Transfer.builder()
                .account(account)
                .bank(Bank.PKO_BP.getName())
                .currency(currency)
                .transferType(transferTypeRecognizer.recognize(transferType, amount).name())
                .date(new SimpleDateFormat("yyyy-MM-dd").parse(date))
                .amount(MoneyConverter.toMoneyValue(getMoneyString(amount)))
                .balance(MoneyConverter.toMoneyValue(getMoneyString(balance)))
                .description(details.getTitle())
                .beneficiaryName(details.getBeneficiaryName())
                .beneficiaryAccount(details.getBeneficiaryAccount())
                .beneficiaryAddress(details.getBeneficiaryAddress())
                .payeeName(details.getPayeeName())
                .payeeAccount(details.getPayeeAccount())
                .payeeAddress(details.getPayeeAddress())
                .cardNumber(details.getCardNumber())
                .build();
    }

    @Getter
    private class TransferDetails {

        private String beneficiaryName;
        private String beneficiaryAddress;
        private String beneficiaryAccount;
        private String title;
        private String cardNumber;
        private String payeeName;
        private String payeeAccount;
        private String payeeAddress;

        TransferDetails(String description) {
            Matcher outgoingPaymentMatcher = outgoingPayment.matcher(description);
            Matcher mobileOutgoingPaymentMatcher = mobileOutgoingPayment.matcher(description);
            Matcher outgoingPaymentWithoutAddressMatcher = outgoingPaymentWithoutAddress.matcher(description);
            Matcher outgoingPaymentWithoutAccountMatcher = outgoingPaymentWithoutAccount.matcher(description);
            Matcher outgoingPaymentWithoutTitleMatcher = outgoingPaymentWithoutTitle.matcher(description);
            Matcher cardPaymentMatcher = cardPayment.matcher(description);
            Matcher incomingPaymentMatcher = incomingPayment.matcher(description);
            Matcher foreignIncomingPaymentMatcher = foreignPayment.matcher(description);
            Matcher onlyTitleMatcher = onlyTitle.matcher(description);
            Matcher incomingPaymentWithoutTitleMatcher = incomingPaymentWithoutTitle.matcher(description);
            Matcher incomingPaymentWithoutNameMatcher = incomingPaymentWithoutName.matcher(description);
            Matcher incomingPaymentWithoutAccountMatcher = incomingPaymentWithoutAccount.matcher(description);
            Matcher incomingPaymentWithoutAddressMatcher = incomingPaymentWithoutAddress.matcher(description);
            Matcher canceledOutgoingPaymentMatcher = canceledOutgoingPayment.matcher(description);
//            def othersMatcher = others.matcher(description)

            if (outgoingPaymentMatcher.matches()) {
                beneficiaryAccount = outgoingPaymentMatcher.group(2).trim();
                beneficiaryName = outgoingPaymentMatcher.group(4).trim();
                beneficiaryAddress = outgoingPaymentMatcher.group(6).trim();
                title = outgoingPaymentMatcher.group(8).trim();
            } else if (outgoingPaymentWithoutAddressMatcher.matches()) {
                beneficiaryAccount = outgoingPaymentWithoutAddressMatcher.group(2).trim();
                beneficiaryName = outgoingPaymentWithoutAddressMatcher.group(4).trim();
                title = outgoingPaymentWithoutAddressMatcher.group(6).trim();
            } else if (mobileOutgoingPaymentMatcher.matches()) {
                beneficiaryName = mobileOutgoingPaymentMatcher.group(2).trim();
                title = mobileOutgoingPaymentMatcher.group(4).trim() + mobileOutgoingPaymentMatcher.group(10).trim();
            } else if (outgoingPaymentWithoutTitleMatcher.matches()) {
                beneficiaryAccount = outgoingPaymentWithoutTitleMatcher.group(2).trim();
                beneficiaryName = outgoingPaymentWithoutTitleMatcher.group(4).trim();
                title = outgoingPaymentWithoutTitleMatcher.group(6).trim();
            } else if (outgoingPaymentWithoutAccountMatcher.matches()) {
                beneficiaryName = outgoingPaymentWithoutAccountMatcher.group(2).trim();
                beneficiaryAddress = outgoingPaymentWithoutAccountMatcher.group(4).trim();
                title = outgoingPaymentWithoutAccountMatcher.group(6).trim();
            } else if (cardPaymentMatcher.matches()) {
                title = cardPaymentMatcher.group(2).trim();
                beneficiaryName = cardPaymentMatcher.group(4).trim();
                cardNumber = cardPaymentMatcher.group(8).trim();
            } else if (incomingPaymentMatcher.matches()) {
                payeeAccount = incomingPaymentMatcher.group(2).trim();
                payeeName = incomingPaymentMatcher.group(4).trim();
                payeeAddress = incomingPaymentMatcher.group(6).trim();
                title = incomingPaymentMatcher.group(8).trim();
            } else if (incomingPaymentWithoutTitleMatcher.matches()) {
                payeeAccount = incomingPaymentWithoutTitleMatcher.group(2).trim();
                payeeName = incomingPaymentWithoutTitleMatcher.group(4).trim();
                payeeAddress = incomingPaymentWithoutTitleMatcher.group(6).trim();
                title = incomingPaymentWithoutTitleMatcher.group(6).trim();
            } else if (incomingPaymentWithoutNameMatcher.matches()) {
                payeeAccount = incomingPaymentWithoutNameMatcher.group(2).trim();
                payeeAddress = incomingPaymentWithoutNameMatcher.group(4).trim();
                title = incomingPaymentWithoutNameMatcher.group(6).trim();
            } else if (incomingPaymentWithoutAccountMatcher.matches()) {
                payeeName = incomingPaymentWithoutAccountMatcher.group(2).trim();
                payeeAddress = incomingPaymentWithoutAccountMatcher.group(4).trim();
                title = incomingPaymentWithoutAccountMatcher.group(6).trim();
            } else if (incomingPaymentWithoutAddressMatcher.matches()) {
                payeeAccount = incomingPaymentWithoutAddressMatcher.group(2).trim();
                payeeName = incomingPaymentWithoutAddressMatcher.group(4).trim();
                title = incomingPaymentWithoutAddressMatcher.group(4).trim();
            } else if (foreignIncomingPaymentMatcher.matches()) {
                payeeAccount = foreignIncomingPaymentMatcher.group(2).trim();
                payeeName = foreignIncomingPaymentMatcher.group(4).trim();
                title = foreignIncomingPaymentMatcher.group(6).trim();
            } else if (canceledOutgoingPaymentMatcher.matches()) {
                title = canceledOutgoingPaymentMatcher.group(0);
            } else if (onlyTitleMatcher.matches()) {
                title = onlyTitleMatcher.group(2);
//            } else if (othersMatcher.matches()){
//                title = othersMatcher.group(0)
            } else {
                title = description;
//                log.info(description)
//                throw new IllegalArgumentException('description does not match any pattern')
            }


        }

    }
}
