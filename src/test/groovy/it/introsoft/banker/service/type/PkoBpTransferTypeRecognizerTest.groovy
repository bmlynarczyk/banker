package it.introsoft.banker.service.type

import it.introsoft.banker.model.jpa.Transfer
import spock.lang.Specification

import static it.introsoft.banker.model.raw.TransferType.*

class PkoBpTransferTypeRecognizerTest extends Specification {

    PkoBpTransferTypeRecognizer transferTypeRecognizer = new PkoBpTransferTypeRecognizer()

    def "should recognize correct transfer type"() {
        when:
        def recognizedType = transferTypeRecognizer.recognize(describer, Transfer.builder().amount(1000).build())
        then:
        recognizedType == expectedType
        where:
        describer                            | expectedType
        'Opłata'                             | BANK_CHARGES
        'Opłata za użytkowanie karty'        | BANK_CHARGES
        'Obciążenie'                         | BANK_CHARGES
        'Zlecenie stałe'                     | STANDING_ORDER_CHARGES
        'Wypłata gotówkowa z kasy'           | CHARGES
        'Wypłata gotówki w POS'              | CHARGES
        'Wypłata w bankomacie - kod mobilny' | ATM_WITHDRAWAL
        'Przelew z rachunku'                 | CHARGES
        'Płatność kartą'                     | CARD_PAYMENT
        'Wypłata z bankomatu'                | ATM_WITHDRAWAL
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
        transferTypeRecognizer.recognize('nieznany', Transfer.builder().amount(1000).build())
        then:
        IllegalStateException ex = thrown()
        ex.message == 'unknown transfer type. describer: nieznany, amount: 1000'
    }

}
