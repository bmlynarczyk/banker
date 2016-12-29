package it.introsoft.banker.model.transfer.type

import spock.lang.Specification

import static it.introsoft.banker.model.transfer.type.TransferType.*

class PkoBpTransferTypeRecognizerTest extends Specification {

    TransferTypeRecognizer transferTypeRecognizer = new PkoBpTransferTypeRecognizer()

    def "should recognize correct transfer type"() {
        when:
        def recognizedType = transferTypeRecognizer.recognize(describer, '1000')
        then:
        recognizedType == expectedType
        where:
        describer                            | expectedType
        'Opłata'                             | BANK_CHARGES
        'Opłata za użytkowanie karty'        | BANK_CHARGES
        'Obciążenie'                         | BANK_CHARGES
        'Zlecenie stałe'                     | CHARGES
        'Wypłata gotówkowa z kasy'           | CHARGES
        'Wypłata gotówki w POS'              | CHARGES
        'Wypłata w bankomacie - kod mobilny' | CHARGES
        'Przelew z rachunku'                 | CHARGES
        'Płatność kartą'                     | CHARGES
        'Wypłata z bankomatu'                | CHARGES
        'Prowizja'                           | CHARGES
        'Podatek od odsetek'                 | INTEREST_TAX_CHARGES
        'Przelew do US'                      | TAX_CHARGES
        'Przelew podatkowy'                  | TAX_CHARGES
        'Naliczenie odsetek'                 | BANK_DEPOSIT
        'Uznanie'                            | DEPOSIT
        'Przelew na rachunek'                | DEPOSIT
        'Zwrot płatności kartą'              | DEPOSIT
        'Korekta'                            | DEPOSIT
        'Wpłata gotówkowa w kasie'           | DEPOSIT
        'Przelew zagraniczny'                | DEPOSIT
    }

    def "should throw exception when incorrect transfer type"() {
        when:
        transferTypeRecognizer.recognize('nieznany', '1000')
        then:
        IllegalStateException ex = thrown()
        ex.message == 'unknown transfer type. describer: nieznany, amount: 1000'
    }

}
