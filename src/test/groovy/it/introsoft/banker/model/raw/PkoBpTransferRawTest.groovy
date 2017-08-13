package it.introsoft.banker.model.raw

import it.introsoft.banker.model.TestData
import it.introsoft.banker.model.jpa.Transfer
import it.introsoft.banker.service.converter.PkoBpTransferRawConverter
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.core.convert.converter.Converter
import spock.lang.Specification

import static it.introsoft.banker.model.raw.TransferRawUtils.toLocalDate

class PkoBpTransferRawTest extends Specification {

    Converter<Document, List<TransferRaw>> converter = new PkoBpTransferRawConverter('1234')

    def "should set required fields"() {
        given:
        Document document = Jsoup.parse(TestData.PKO_BP_TRANSFER_TABLE)
        when:
        Transfer transfer = converter.convert(document).first().asTransfer()
        then:
        transfer.account == '1234'
        transfer.beneficiaryAccount == '57 1140 2004 0000 0000 0000 0000'
        transfer.beneficiaryName == 'JAN NOWAK'
        transfer.beneficiaryAddress == 'XXX XXX'
        transfer.description == 'XXX'
        transfer.transferType == TransferType.DEPOSIT
        transfer.date == toLocalDate('2016-12-16', 'yyyy-MM-dd')
        transfer.bank == Bank.PKO_BP
        transfer.amount == 2500000L
        transfer.balance == 3082010L
        transfer.currency == 'PLN'
    }

}
