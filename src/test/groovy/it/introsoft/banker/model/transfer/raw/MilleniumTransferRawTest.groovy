package it.introsoft.banker.model.transfer.raw

import it.introsoft.banker.model.Bank
import it.introsoft.banker.model.TestData
import it.introsoft.banker.model.transfer.Transfer
import it.introsoft.banker.model.transfer.raw.converter.MilleniumTransferRawConverter
import it.introsoft.banker.model.transfer.type.TransferType
import org.springframework.core.convert.converter.Converter
import spock.lang.Specification

class MilleniumTransferRawTest extends Specification {

    Converter<List<String>, TransferRaw> converter = new MilleniumTransferRawConverter('1234')

    def "should set required fields"() {
        when:
        Transfer transfer = converter.convert(TestData.MILLENIUM_CARD_PAYMENTS_TRANSFER_LINES).asTransfer()
        then:
        transfer.account == '1234'
        transfer.beneficiaryAccount == null
        transfer.description == 'Stolowka Lublin 16/12/07'
        transfer.type == TransferType.CHARGES.name()
        transfer.date == new Date().parse('yyyy-MM-dd', '2016-12-09')
        transfer.bank == Bank.MILLENIUM.name
        transfer.amount == new Long('-15100')
        transfer.currency == 'PLN'
    }

}
