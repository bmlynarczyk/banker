package it.introsoft.banker.service.converter

import it.introsoft.banker.model.TestData
import it.introsoft.banker.model.raw.PkoBpTransferRaw
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import spock.lang.Specification

class PkoBpTransferRawConverterTest extends Specification {

    def 'convert pko html table to transfer raws'() {
        given:
        Document document = Jsoup.parse(TestData.PKO_BP_TRANSFER_TABLE)
        when:
        def transferRaws = new PkoBpTransferRawConverter('abc').convert(document)
        then:
        !transferRaws.isEmpty()
        PkoBpTransferRaw transferRaw = transferRaws.first()
        transferRaw.account == 'abc'
        transferRaw.date == '2016-12-16'
        transferRaw.transferType == 'Przelew na rachunek'
        transferRaw.description == 'Rachunek odbiorcy : 57 1140 2004 0000 0000 0000 0000 Nazwa odbiorcy : JAN NOWAK Adres odbiorcy : XXX XXX Tytuł : XXX'
        transferRaw.amount == '+2500.00'
        transferRaw.balance == '+3082.01'
    }

}
