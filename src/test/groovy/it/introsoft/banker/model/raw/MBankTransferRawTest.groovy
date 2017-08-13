package it.introsoft.banker.model.raw

import it.introsoft.banker.model.TestData
import it.introsoft.banker.model.jpa.Transfer
import it.introsoft.banker.service.converter.MBankTransferRawConverter
import org.springframework.core.convert.converter.Converter
import spock.lang.Specification

import static java.time.LocalDate.parse

class MBankTransferRawTest extends Specification {

    Converter<List<String>, MBankTransferRaw> converter = new MBankTransferRawConverter('1234')

    def "should set required fields"() {
        when:
        Transfer transfer = converter.convert(TestData.M_BANK_DEPOSIT).asTransfer()
        then:
        transfer.account == '1234'
        transfer.beneficiaryAccount == '57 1140 2004 0000 0000 0000 0000'
        transfer.bank == Bank.M_BANK
        transfer.transferType == TransferType.DEPOSIT
        transfer.date == parse('2016-07-28')
        transfer.amount == 2100000L
        transfer.description == 'FAKTURA TELEFON'
    }

}
