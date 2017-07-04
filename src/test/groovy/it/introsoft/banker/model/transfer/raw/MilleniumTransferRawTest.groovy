package it.introsoft.banker.model.transfer.raw

import it.introsoft.banker.model.Bank
import it.introsoft.banker.model.TestData
import it.introsoft.banker.model.transfer.raw.converter.MilleniumTransferRawConverter
import it.introsoft.banker.model.transfer.type.TransferType
import it.introsoft.banker.repository.Transfer
import org.springframework.core.convert.converter.Converter
import spock.lang.Specification

class MilleniumTransferRawTest extends Specification {

    Converter<List<String>, TransferRaw> converter = new MilleniumTransferRawConverter('1234')

    def "should set required fields for card payment"() {
        when:
        Transfer transfer = converter.convert(TestData.MILLENIUM_CARD_PAYMENTS).asTransfer()
        then:
        transfer.account == '1234'
        transfer.transferType == TransferType.CHARGES.name()
        transfer.dateTransferNumber == 3L
        transfer.date == new Date().parse('yyyy-MM-dd', '2016-12-09')
        transfer.payeeAccount == '98116022020000000000000000'
        transfer.cardNumber == 'XXXXXXXXXXXXXXXX'
        transfer.description == 'Stolowka Lublin 16/12/07'
        transfer.bank == Bank.MILLENIUM.name
        transfer.amount == new Long('-15100')
        transfer.currency == 'PLN'

        transfer.beneficiaryAccount == null
        transfer.beneficiaryName == null
        transfer.beneficiaryAddress == null
        transfer.payeeName == null
        transfer.payeeAddress == null
        transfer.category == null
        transfer.balance == null
    }

    def "should set required fields for transfer to another bank"() {
        when:
        Transfer transfer = converter.convert(TestData.MILLENIUM_TRANSFER_TO_ANOTHER_BANK).asTransfer()
        then:
        transfer.transferType == TransferType.CHARGES.name()
        transfer.dateTransferNumber == 1L
        transfer.date == new Date().parse('yyyy-MM-dd', '2016-09-21')
        transfer.payeeAccount == '98116022020000000000000000'
        transfer.payeeName == 'JAN NOWAK LUBLIN 348 22-035'
        transfer.beneficiaryAccount == '59102031500000000000000000'
        transfer.beneficiaryName == 'John Doe'
        transfer.amount == new Long('-1000000')
        transfer.currency == 'PLN'
        transfer.description == 'Przelew - John Doe'
        transfer.account == '1234'
    }

}
