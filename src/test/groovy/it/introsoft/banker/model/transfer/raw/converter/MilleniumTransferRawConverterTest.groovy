package it.introsoft.banker.model.transfer.raw.converter

import it.introsoft.banker.model.TestData
import it.introsoft.banker.model.transfer.raw.MilleniumTransferRaw
import org.springframework.core.convert.converter.Converter
import spock.lang.Specification

class MilleniumTransferRawConverterTest extends Specification {

    Converter<List<String>, MilleniumTransferRaw> converter = new MilleniumTransferRawConverter('1234')

    def "should set required fields with accounted amount"() {
        when:
        MilleniumTransferRaw transferRaw = converter.convert(TestData.MILLENIUM_CARD_PAYMENTS_TRANSFER_LINES)
        then:
        transferRaw.account == '1234'
        transferRaw.beneficiaryAccount == null
        transferRaw.title == 'Tytuł Stolowka Lublin 16/12/07'
        transferRaw.type == 'Typ operacji TRANSAKCJA KARTĄ PŁATNICZĄ'
        transferRaw.date == 'Data księgowania 2016-12-09'
        transferRaw.accountedAmount == 'Kwota zaksięgowana -15,10 PLN'
    }

    def "should set required fields with amount"() {
        when:
        MilleniumTransferRaw transferRaw = converter.convert(TestData.MILLENIUM_CARD_PAYMENTS_TRANSFER_LINES)
        then:
        transferRaw.account == '1234'
        transferRaw.beneficiaryAccount == null
        transferRaw.title == 'Tytuł Stolowka Lublin 16/12/07'
        transferRaw.type == 'Typ operacji TRANSAKCJA KARTĄ PŁATNICZĄ'
        transferRaw.date == 'Data księgowania 2016-12-09'
        transferRaw.accountedAmount == 'Kwota zaksięgowana -15,10 PLN'
    }

}
