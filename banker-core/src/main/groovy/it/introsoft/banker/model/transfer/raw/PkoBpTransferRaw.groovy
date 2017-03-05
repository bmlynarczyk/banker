package it.introsoft.banker.model.transfer.raw

import groovy.transform.EqualsAndHashCode
import groovy.util.logging.Slf4j
import it.introsoft.banker.model.Bank
import it.introsoft.banker.model.transfer.Transfer
import it.introsoft.banker.model.transfer.type.TransferTypeRecognizer

import java.util.regex.Matcher
import java.util.regex.Pattern

import static it.introsoft.banker.model.transfer.supplier.MoneyConverter.toMoneyValue

@Slf4j
@EqualsAndHashCode
class PkoBpTransferRaw implements TransferRaw {

    private static final Pattern outgoingPayment = Pattern.compile('(Rachunek odbiorcy : (.*))(Nazwa odbiorcy : (.*))(Adres odbiorcy : (.*))(Tytuł : (.*))')
    private static final Pattern mobileOutgoingPayment = Pattern.compile('(Numer telefonu : (.*))(Lokalizacja : (.*))(Data i czas operacji : (.*))(Bankomat : (.*))(Numer referencyjny : (.*))')
    private static final Pattern outgoingPaymentWithoutAddress = Pattern.compile('(Rachunek odbiorcy : (.*))(Nazwa odbiorcy : (.*))(Tytuł : (.*))')
    private static final Pattern outgoingPaymentWithoutAccount = Pattern.compile('(Nazwa odbiorcy : (.*))(Adres odbiorcy : (.*))(Tytuł : (.*))')
    private static final Pattern outgoingPaymentWithoutTitle = Pattern.compile('(Rachunek odbiorcy : (.*))(Nazwa odbiorcy : (.*))(Adres odbiorcy : (.*))')
    private static final Pattern canceledOutgoingPayment = Pattern.compile('PRZELEW WYCHODZĄCY ANUL.')
    private static final Pattern cardPayment = Pattern.compile('(Tytuł : (.*))(Lokalizacja : (.*))(Data i czas operacji : (.*))(Oryginalna kwota operacji : (.*))(Numer karty : (.*))')
    private static final Pattern incomingPayment = Pattern.compile('(Rachunek nadawcy : (.*))(Nazwa nadawcy : (.*))(Adres nadawcy : (.*))(Tytuł : (.*))')
    private static final Pattern incomingPaymentWithoutAccount = Pattern.compile('(Nazwa nadawcy : (.*))(Adres nadawcy : (.*))(Tytuł : (.*))')
    private static final Pattern incomingPaymentWithoutName = Pattern.compile('(Rachunek nadawcy : (.*))(Adres nadawcy : (.*))(Tytuł : (.*))')
    private static final Pattern incomingPaymentWithoutTitle = Pattern.compile('(Rachunek nadawcy : (.*))(Nazwa nadawcy : (.*))(Adres nadawcy : (.*))')
    private static final Pattern incomingPaymentWithoutAddress = Pattern.compile('(Rachunek nadawcy : (.*))(Nazwa nadawcy : (.*))(Tytuł : (.*))')
    private static final Pattern foreignPayment = Pattern.compile('(Rachunek nadawcy : (.*))(Nazwa nadawcy : (.*))(Tytuł : (.*))(Kwota w walucie oryginalnej : (.*))(Kurs przewalutowania : (.*))(Instrukcja kosztowa : (.*))(Referencje polecenia wypłaty : (.*))')
//    private static final Pattern others = Pattern.compile('OPŁATA ZA KARTĘ KARTA \\d\\*\\*\\*\\*\\*\\*\\*\\*\\d\\d\\d\\d\\d\\d\\d|NICK TOPA 87, WIATRÓWKA B3 STG|OP.PROW.RACH.|OPŁ.ZA BAN.EL.|ROZL OPROC DEBETU \\d\\d\\d\\d-\\d\\d-\\d\\d|OPŁ. ZA KARTĘ ZGODNIE Z TPIO \\d\\*\\*\\*\\*\\*\\*\\*\\*\\d\\d\\d\\d\\d\\d\\d|OPŁATA PRZELEW ZEW.DOWOL.|ABONAMENT IPKO|ZESTAW 740|RACHUNEK OSZCZĘDNOŚCIOWY|BARTŁOMIEJ MŁYNARCZYK OPŁATA Z|WYPŁATA ARI494712|PODATEK OD ODSETEK KAPITAŁOWYCH|KAPITALIZACJA ODSETEK|MŁYNARCZYK BARTŁOMIEJ|OPŁATA ZA PROWADZENIE RACHUNKU|KAPIT.ODSETEK KARNYCH-OBCIĄŻENIE|ARI494712 WYPLATA')
    private static final Pattern onlyTitle = Pattern.compile('(Tytuł : (.*))')


    String account
    String description
    String transferType
    String date
    String amount
    String currency
    String balance

    private static final TransferTypeRecognizer transferTypeRecognizer = Bank.PKO_BP.typeRecognizer()

    @Override
    Transfer asTransfer() {
        def details = new TransferDetails(description)
        return new Transfer(
                account: account,
                bank: Bank.PKO_BP.name,
                currency: currency,
                transferType: transferTypeRecognizer.recognize(transferType, amount),
                date: new Date().parse('yyyy-MM-dd', date),
                amount: toMoneyValue(getMoneyString(amount)),
                balance: toMoneyValue(getMoneyString(balance)),
                description: details.title,
                beneficiaryName: details.beneficiaryName,
                beneficiaryAccount: details.beneficiaryAccount,
                beneficiaryAddress: details.beneficiaryAddress,
                payeeName: details.payeeName,
                payeeAccount: details.payeeAccount,
                payeeAddress: details.payeeAddress,
                cardNumber: details.cardNumber
        )
    }

    private class TransferDetails {

        TransferDetails(String description) {
            def outgoingPaymentMatcher = outgoingPayment.matcher(description)
            def mobileOutgoingPaymentMatcher = mobileOutgoingPayment.matcher(description)
            def outgoingPaymentWithoutAddressMatcher = outgoingPaymentWithoutAddress.matcher(description)
            def outgoingPaymentWithoutAccountMatcher = outgoingPaymentWithoutAccount.matcher(description)
            def outgoingPaymentWithoutTitleMatcher = outgoingPaymentWithoutTitle.matcher(description)
            def cardPayment = cardPayment.matcher(description)
            def incomingPaymentMatcher = incomingPayment.matcher(description)
            def foreignIncomingPaymentMatcher = foreignPayment.matcher(description)
            def onlyTitleMatcher = onlyTitle.matcher(description)
            def incomingPaymentWithoutTitleMatcher = incomingPaymentWithoutTitle.matcher(description)
            def incomingPaymentWithoutNameMatcher = incomingPaymentWithoutName.matcher(description)
            def incomingPaymentWithoutAccountMatcher = incomingPaymentWithoutAccount.matcher(description)
            def incomingPaymentWithoutAddressMatcher = incomingPaymentWithoutAddress.matcher(description)
            def canceledOutgoingPaymentMatcher = canceledOutgoingPayment.matcher(description)
//            def othersMatcher = others.matcher(description)

            if (outgoingPaymentMatcher.matches()) {
                beneficiaryAccount = outgoingPaymentMatcher.group(2).trim()
                beneficiaryName = outgoingPaymentMatcher.group(4).trim()
                beneficiaryAddress = outgoingPaymentMatcher.group(6).trim()
                title = outgoingPaymentMatcher.group(8).trim()
            } else if(outgoingPaymentWithoutAddressMatcher.matches()) {
                beneficiaryAccount = outgoingPaymentWithoutAddressMatcher.group(2).trim()
                beneficiaryName = outgoingPaymentWithoutAddressMatcher.group(4).trim()
                title = outgoingPaymentWithoutAddressMatcher.group(6).trim()
            } else if(mobileOutgoingPaymentMatcher.matches()) {
                beneficiaryName = mobileOutgoingPaymentMatcher.group(2).trim()
                title = mobileOutgoingPaymentMatcher.group(4).trim() + mobileOutgoingPaymentMatcher.group(10).trim()
            } else if(outgoingPaymentWithoutTitleMatcher.matches()) {
                beneficiaryAccount = outgoingPaymentWithoutTitleMatcher.group(2).trim()
                beneficiaryName = outgoingPaymentWithoutTitleMatcher.group(4).trim()
                title = outgoingPaymentWithoutTitleMatcher.group(6).trim()
            } else if(outgoingPaymentWithoutAccountMatcher.matches()) {
                beneficiaryName = outgoingPaymentWithoutAccountMatcher.group(2).trim()
                beneficiaryAddress = outgoingPaymentWithoutAccountMatcher.group(4).trim()
                title = outgoingPaymentWithoutAccountMatcher.group(6).trim()
            } else if(cardPayment.matches()) {
                title = cardPayment.group(2).trim()
                beneficiaryName = cardPayment.group(4).trim()
                cardNumber = cardPayment.group(8).trim()
            } else if (incomingPaymentMatcher.matches()){
                payeeAccount = incomingPaymentMatcher.group(2).trim()
                payeeName = incomingPaymentMatcher.group(4).trim()
                payeeAddress = incomingPaymentMatcher.group(6).trim()
                title = incomingPaymentMatcher.group(8).trim()
            } else if (incomingPaymentWithoutTitleMatcher.matches()){
                payeeAccount = incomingPaymentWithoutTitleMatcher.group(2).trim()
                payeeName = incomingPaymentWithoutTitleMatcher.group(4).trim()
                payeeAddress = incomingPaymentWithoutTitleMatcher.group(6).trim()
                title = incomingPaymentWithoutTitleMatcher.group(6).trim()
            } else if (incomingPaymentWithoutNameMatcher.matches()){
                payeeAccount = incomingPaymentWithoutNameMatcher.group(2).trim()
                payeeAddress = incomingPaymentWithoutNameMatcher.group(4).trim()
                title = incomingPaymentWithoutNameMatcher.group(6).trim()
            } else if (incomingPaymentWithoutAccountMatcher.matches()){
                payeeName = incomingPaymentWithoutAccountMatcher.group(2).trim()
                payeeAddress = incomingPaymentWithoutAccountMatcher.group(4).trim()
                title = incomingPaymentWithoutAccountMatcher.group(6).trim()
            } else if (incomingPaymentWithoutAddressMatcher.matches()){
                payeeAccount = incomingPaymentWithoutAddressMatcher.group(2).trim()
                payeeName = incomingPaymentWithoutAddressMatcher.group(4).trim()
                title = incomingPaymentWithoutAddressMatcher.group(4).trim()
            } else if (foreignIncomingPaymentMatcher.matches()) {
                payeeAccount = foreignIncomingPaymentMatcher.group(2).trim()
                payeeName = foreignIncomingPaymentMatcher.group(4).trim()
                title = foreignIncomingPaymentMatcher.group(6).trim()
            } else if (canceledOutgoingPaymentMatcher.matches()){
                title = canceledOutgoingPaymentMatcher.group(0)
            } else if (onlyTitleMatcher.matches()){
                title = onlyTitleMatcher.group(2)
//            } else if (othersMatcher.matches()){
//                title = othersMatcher.group(0)
            } else {
                title = description
//                log.info(description)
//                throw new IllegalArgumentException('description does not match any pattern')
            }

        }

        final String beneficiaryName
        final String beneficiaryAddress
        final String beneficiaryAccount
        final String title
        final String cardNumber
        final String payeeName
        final String payeeAccount
        final String payeeAddress

    }

    private static String getMoneyString(String value) {
        return value.replaceAll('\\+', '').replaceAll('\\.', ',')
    }


}
