package it.introsoft.banker.model.raw

import it.introsoft.banker.model.TestData
import it.introsoft.banker.model.jpa.Transfer
import it.introsoft.banker.service.converter.BgzOptimaTransferRawConverter
import org.springframework.core.convert.converter.Converter
import spock.lang.Specification

import static it.introsoft.banker.model.raw.TransferRawUtils.toLocalDate

class BgzOptimaTransferRawTest extends Specification {

    Converter<List<String>, List<BgzOptimaTransferRaw>> converter = new BgzOptimaTransferRawConverter('1234')

    def "should set required fields"() {
        when:
        Transfer transfer = converter.convert(TestData.BGZ_OPTIMA_MULTI_TRANSFER_PAGE).first().asTransfer()
        then:
        transfer.account == '1234'
        transfer.bank == Bank.BGZ_OPTIMA
        transfer.transferType == TransferType.BANK_DEPOSIT
        transfer.date == toLocalDate('30-04-2013', 'dd-MM-yyyy')
        transfer.amount == 15200L
        transfer.balance == 5016980L
    }

}
