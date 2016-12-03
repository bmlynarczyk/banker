package it.introsoft.banker.model.transfer.raw

import it.introsoft.banker.model.Bank
import it.introsoft.banker.model.TestData
import it.introsoft.banker.model.transfer.Transfer
import it.introsoft.banker.model.transfer.raw.converter.BgzOptimaTransferRawConverter
import it.introsoft.banker.model.transfer.type.TransferType
import org.springframework.core.convert.converter.Converter
import spock.lang.Specification

class BgzOptimaTransferRawTest extends Specification {

    Converter<List<String>, List<BgzOptimaTransferRaw>> converter = new BgzOptimaTransferRawConverter('1234')

    def "should set required fields"() {
        when:
        Transfer transfer = converter.convert(TestData.BGZ_OPTIMA_MULTI_TRANSFER_PAGE).first().asTransfer()
        then:
        transfer.account == '1234'
        transfer.bank == Bank.BGZ_OPTIMA.name
        transfer.type == TransferType.BANK_DEPOSIT.name()
        transfer.date == new Date().parse('dd-MM-yyyy', '30-04-2013')
        transfer.amount ==  15200L
        transfer.balance ==  5016980L
    }

}
