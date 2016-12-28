package it.introsoft.banker.model.transfer.raw.converter

import it.introsoft.banker.model.TestData
import it.introsoft.banker.model.transfer.raw.MBankTransferRaw
import org.springframework.core.convert.converter.Converter
import spock.lang.Specification

class MBankTransferRawConverterTest extends Specification {

    Converter<List<String>, MBankTransferRaw> converter = new MBankTransferRawConverter('1234')

    def "should set required fields"() {
        when:
        MBankTransferRaw transferRaw = converter.convert(TestData.M_BANK_DEPOSIT)
        then:
        transferRaw.account == '1234'
        transferRaw.accounts == 'Nr Rachunku: 11 1020 3176 0000 0000 0000 0000 Nr Rachunku: 57 1140 2004 0000 0000 0000 0000'
        transferRaw.sender == 'Nadawca: JAN KOWALSKI Odbiorca: JAN KOWALSKI'
        transferRaw.transferType == 'Rodzaj operacji: PRZELEW ZEWNĘTRZNY PRZYCHODZĄCY'
        transferRaw.date == 'Data księgowania: 2016-07-28'
        transferRaw.amount == 'Kwota przelewu: 2 100,00 PLN'
        transferRaw.title == 'Tytuł operacji: FAKTURA TELEFON'
        transferRaw.symbol == null
    }

}
