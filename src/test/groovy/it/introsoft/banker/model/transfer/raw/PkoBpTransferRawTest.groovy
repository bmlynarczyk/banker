package it.introsoft.banker.model.transfer.raw

import it.introsoft.banker.model.Bank
import it.introsoft.banker.model.TestData
import it.introsoft.banker.model.transfer.raw.converter.PkoBpTransferRawConverter
import it.introsoft.banker.model.transfer.type.TransferType
import it.introsoft.banker.repository.Transfer
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.core.convert.converter.Converter
import spock.lang.Specification

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
        transfer.transferType == TransferType.DEPOSIT.name()
        transfer.date == new Date().parse('yyyy-MM-dd', '2016-12-16')
        transfer.bank == Bank.PKO_BP.name
        transfer.amount == 2500000L
        transfer.balance == 3082010L
        transfer.currency == 'PLN'
    }

}
