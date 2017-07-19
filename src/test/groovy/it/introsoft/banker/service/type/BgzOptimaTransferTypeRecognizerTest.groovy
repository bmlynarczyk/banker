package it.introsoft.banker.service.type

import it.introsoft.banker.model.raw.TransferType
import spock.lang.Specification

class BgzOptimaTransferTypeRecognizerTest extends Specification {

    TransferTypeRecognizer transferTypeRecognizer = new BgzOptimaTransferTypeRecognizer()

    def 'should recognize bank deposit'() {
        when:
        TransferType transferType = transferTypeRecognizer.recognize('ODSETKI', '100')
        then:
        transferType == TransferType.BANK_DEPOSIT
    }

    def 'should recognize deposit'() {
        when:
        TransferType transferType = transferTypeRecognizer.recognize(description, '100')
        then:
        transferType == TransferType.DEPOSIT
        where:
        description       | _
        'PRZELEW'         | _
        'CHARGES/DEPOSIT' | _

    }

    def 'should recognize charges'() {
        when:
        TransferType transferType = transferTypeRecognizer.recognize('PRZELEW', '-100')
        then:
        transferType == TransferType.CHARGES
    }

    def 'should recognize tax charges'() {
        when:
        TransferType transferType = transferTypeRecognizer.recognize('PODATEK', '-100')
        then:
        transferType == TransferType.INTEREST_TAX_CHARGES
    }

}
