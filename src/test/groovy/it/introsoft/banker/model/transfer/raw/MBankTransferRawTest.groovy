package it.introsoft.banker.model.transfer.raw

import it.introsoft.banker.model.Bank
import it.introsoft.banker.model.TestData
import it.introsoft.banker.model.transfer.Transfer
import it.introsoft.banker.model.transfer.raw.converter.MBankTransferRawConverter
import it.introsoft.banker.model.transfer.type.TransferType
import org.springframework.core.convert.converter.Converter
import spock.lang.Specification

class MBankTransferRawTest extends Specification {

    Converter<List<String>, MBankTransferRaw> converter = new MBankTransferRawConverter('1234')

    def "should set required fields"() {
        when:
        Transfer transfer = converter.convert(TestData.M_BANK_DEPOSIT).asTransfer()
        then:
        transfer.account == '1234'
        transfer.beneficiaryAccount == '57 1140 2004 0000 0000 0000 0000'
        transfer.bank == Bank.M_BANK.name
        transfer.transferType == TransferType.DEPOSIT.name()
        transfer.date == new Date().parse('yyyy-MM-dd', '2016-07-28')
        transfer.amount ==  2100000L
        transfer.description == 'FAKTURA TELEFON'
    }

}
