package it.introsoft.banker.service.converter

import it.introsoft.banker.model.TestData
import it.introsoft.banker.model.raw.MilleniumTransferRaw
import org.springframework.core.convert.converter.Converter
import spock.lang.Specification

class MilleniumTransferRawConverterTest extends Specification {

    Converter<List<String>, MilleniumTransferRaw> converter = new MilleniumTransferRawConverter('98116022020000000000000000')

    def "should set required fields for card payment"() {
        when:
        MilleniumTransferRaw transferRaw = converter.convert(TestData.MILLENIUM_CARD_PAYMENTS)
        then:
        transferRaw.transferType == 'Typ operacji TRANSAKCJA KARTĄ PŁATNICZĄ'
        transferRaw.dateTransferNumber == 'Dzienny numer transakcji 3'
        transferRaw.date == 'Data księgowania 2016-12-09'
        transferRaw.accountingDate == 'Data waluty 2016-12-09'
        transferRaw.payeeAccount == 'Z rachunku 98116022020000000000000000'
        transferRaw.cardName == 'Karta Visa Konto 360'
        transferRaw.cardNumber == 'Numer karty XXXXXXXXXXXXXXXX'
        transferRaw.cardOwner == 'Posiadacz karty JAN KOWALSKI'
        transferRaw.accountedAmount == 'Kwota zaksięgowana -15,10 PLN'
        transferRaw.title == 'Tytuł Stolowka Lublin 16/12/07'
        transferRaw.account == '98116022020000000000000000'

        transferRaw.beneficiaryAccount == null
        transferRaw.beneficiaryName == null
        transferRaw.payeeName == null

    }

    def "should set required fields for transfer to another bank"() {
        when:
        MilleniumTransferRaw transferRaw = converter.convert(TestData.MILLENIUM_TRANSFER_TO_ANOTHER_BANK)
        then:
        transferRaw.transferType == 'Typ operacji PRZELEW DO INNEGO BANKU'
        transferRaw.dateTransferNumber == 'Dzienny numer transakcji 1'
        transferRaw.date == 'Data księgowania 2016-09-21'
        transferRaw.transferDate == 'Data transakcji 2016-09-20'
        transferRaw.payeeAccount == 'Z rachunku 98116022020000000000000000'
        transferRaw.payeeName == 'Zleceniodawca JAN NOWAK LUBLIN 348 22-035'
        transferRaw.beneficiaryAccount == 'Na rachunek 59102031500000000000000000'
        transferRaw.beneficiaryName == 'Odbiorca John Doe'
        transferRaw.accountedAmount == 'Kwota zaksięgowana -1 000,00 PLN'
        transferRaw.title == 'Tytuł Przelew - John Doe'
        transferRaw.account == '98116022020000000000000000'

        transferRaw.cardName == null
        transferRaw.cardNumber == null
        transferRaw.cardOwner == null

    }

    def "should set required fields for transfer from another bank"() {
        when:
        MilleniumTransferRaw transferRaw = converter.convert(TestData.MILLENIUM_TRANSFER_FROM_ANOTHER_BANK)
        then:
        transferRaw.transferType == 'Typ operacji PRZELEW PRZYCHODZĄCY'
        transferRaw.dateTransferNumber == 'Dzienny numer transakcji 4'
        transferRaw.date == 'Data księgowania 2016-09-19'
        transferRaw.accountingDate == 'Data waluty 2016-09-19'
        transferRaw.payeeAccount == 'Z rachunku 57114020000000000000000000'
        transferRaw.payeeName == 'Zleceniodawca JAN NOWAK LUBLIN 348 22-035'
        transferRaw.beneficiaryAccount == 'Na rachunek 98116022020000000000000000'
        transferRaw.beneficiaryName == 'Odbiorca BART'
        transferRaw.accountedAmount == 'Kwota zaksięgowana 18 000,00 PLN'
        transferRaw.title == 'Tytuł PRZELEW ŚRODKÓW'
        transferRaw.account == '98116022020000000000000000'

        transferRaw.cardName == null
        transferRaw.cardNumber == null
        transferRaw.cardOwner == null
        transferRaw.transferDate == null

    }

}
