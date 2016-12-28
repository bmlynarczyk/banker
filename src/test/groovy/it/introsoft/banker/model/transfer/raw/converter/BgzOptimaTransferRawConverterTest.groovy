package it.introsoft.banker.model.transfer.raw.converter

import it.introsoft.banker.model.TestData
import it.introsoft.banker.model.transfer.raw.BgzOptimaTransferRaw
import it.introsoft.banker.model.transfer.raw.TransferRaw
import org.springframework.core.convert.converter.Converter
import spock.lang.Specification

class BgzOptimaTransferRawConverterTest extends Specification {

    Converter<List<String>, List<TransferRaw>> converter = new BgzOptimaTransferRawConverter('abc')

    def "should convert multi transfer page into transfer raw collection"() {
        when:
        def raws = converter.convert(TestData.BGZ_OPTIMA_MULTI_TRANSFER_PAGE)
        then:
        !raws.isEmpty()
        def transferRaw = raws.first() as BgzOptimaTransferRaw
        transferRaw.date == '30.04.2013'
        transferRaw.transferType == 'ODSETKI OTRZYMANE'
        transferRaw.amount == '15,20 PLN'
        transferRaw.account == 'abc'
        transferRaw.balance == '5 016,98 PLN'
    }

}
