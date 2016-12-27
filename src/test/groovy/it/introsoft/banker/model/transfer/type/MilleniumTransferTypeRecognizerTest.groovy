package it.introsoft.banker.model.transfer.type

import spock.lang.Specification

import static it.introsoft.banker.model.transfer.type.TransferType.*

class MilleniumTransferTypeRecognizerTest extends Specification {

    TransferTypeRecognizer transferTypeRecognizer = new MilleniumTransferTypeRecognizer()

    def "should recognize correct transfer type"() {
        when:
        def recognizedType = transferTypeRecognizer.recognize(describer, '-1000')
        then:
        recognizedType == expectedType
        where:
        describer                         | expectedType
        'OPŁATA'                          | BANK_CHARGES
        'TRANSAKCJA KARTĄ PŁATNICZĄ'      | CHARGES
        'WYPŁATA KARTĄ Z BANKOMATU'       | CHARGES
        'PRZELEW DO INNEGO BANKU'         | CHARGES
        'PŁATNOŚĆ INTERNETOWA WYCHODZĄCA' | CHARGES
        'PRZELEW WEWNĘTRZNY WYCHODZĄCY'   | CHARGES
        'OPERACJE NA LOKATACH'            | CHARGES
        'PRZELEW PRZYCHODZĄCY'            | DEPOSIT
        'PRZELEW WEWNĘTRZNY PRZYCHODZĄCY' | DEPOSIT
    }

    def "should throw exception when incorrect transfer type"() {
        when:
        transferTypeRecognizer.recognize(describer, '1000')
        then:
        IllegalStateException ex = thrown()
        ex.message == "unknown transfer type. describer: $expectedUnknownType, amount: 1000"
        where:
        describer              | expectedUnknownType
        'OPERACJE NA LOKATACH' | 'OPERACJE NA LOKATACH'
        'nieznany'             | 'nieznany'
    }

}
