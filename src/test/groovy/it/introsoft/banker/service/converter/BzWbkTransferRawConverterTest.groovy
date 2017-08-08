package it.introsoft.banker.service.converter

import spock.lang.Specification

import static it.introsoft.banker.model.raw.TransferType.*

class BzWbkTransferRawConverterTest extends Specification {

    def "should convert version 1 of deposit string to raw"() {
        given:
        def converter = new BzWbkTransferRawConverter("0123")
        String csvLine = "03-08-2017|04-08-2017|test|test|92 1910 1048 2305 0160 0168 0005|2898,63|300,00|10|"
        when:
        def raw = converter.convert(csvLine)
        then:
        raw != null
        def transfer = raw.asTransfer()
        transfer.transferType == DEPOSIT
        transfer.balance == 300000
        transfer.amount == 2898630

    }

    def "should convert version 2 of deposit string to raw"() {
        given:
        def converter = new BzWbkTransferRawConverter("0123")
        String csvLine = "03-08-2017|04-08-2017|V. SOL 444444******4444 ZWROT PŁATNOŚCI KARTĄ|||49,99|300,00|10|"
        when:
        def raw = converter.convert(csvLine)
        then:
        raw != null
        def transfer = raw.asTransfer()
        transfer.transferType == DEPOSIT
        transfer.balance == 300000
        transfer.amount == 49990

    }

    def "should convert version 3 of deposit string to raw"() {
        given:
        def converter = new BzWbkTransferRawConverter("0123")
        String csvLine = "03-08-2017|04-08-2017|PRZENIESIENIE SALDA|||1,20|300,00|10|"
        when:
        def raw = converter.convert(csvLine)
        then:
        raw != null
        def transfer = raw.asTransfer()
        transfer.transferType == DEPOSIT
        transfer.balance == 300000
        transfer.amount == 1200

    }

    def "should convert version 1 of card payment string to raw"() {
        given:
        def converter = new BzWbkTransferRawConverter("0123")
        String csvLine = "03-08-2017|04-08-2017|V. SOL 444444******4444 PŁATNOŚĆ KARTĄ|||-3,29|300,00|10|"
        when:
        def raw = converter.convert(csvLine)
        then:
        raw != null
        def transfer = raw.asTransfer()
        transfer.transferType == CARD_PAYMENT
        transfer.balance == 300000
        transfer.amount == -3290

    }

    def "should convert version 1 of atm withdrawal string to raw"() {
        given:
        def converter = new BzWbkTransferRawConverter("0123")
        String csvLine = "03-08-2017|04-08-2017|V. SOL 444444******4444 WYPŁATA Z BANKOMATU KARTĄ|||-50,00|300,00|10|"
        when:
        def raw = converter.convert(csvLine)
        then:
        raw != null
        def transfer = raw.asTransfer()
        transfer.transferType == ATM_WITHDRAWAL
        transfer.balance == 300000
        transfer.amount == -50000

    }

    def "should convert version 1 of charges string to raw"() {
        given:
        def converter = new BzWbkTransferRawConverter("0123")
        String csvLine = "03-08-2017|04-08-2017|test|test|11 1020 3176 0000 5302 0075 7047|-2000,00|300,00|10|"
        when:
        def raw = converter.convert(csvLine)
        then:
        raw != null
        def transfer = raw.asTransfer()
        transfer.transferType == CHARGES
        transfer.balance == 300000
        transfer.amount == -2000000

    }

    def "should convert version 2 of charges string to raw"() {
        given:
        def converter = new BzWbkTransferRawConverter("0123")
        String csvLine = "03-08-2017|04-08-2017|WYPŁATA|||-200,00|300,00|10|"
        when:
        def raw = converter.convert(csvLine)
        then:
        raw != null
        def transfer = raw.asTransfer()
        transfer.transferType == CHARGES
        transfer.balance == 300000
        transfer.amount == -200000

    }

    def "should convert version 1 of bank_charges string to raw"() {
        given:
        def converter = new BzWbkTransferRawConverter("0123")
        String csvLine = "03-08-2017|04-08-2017|Opłata za prowadzenie rachunku|||-2,00|300,00|10|"
        when:
        def raw = converter.convert(csvLine)
        then:
        raw != null
        def transfer = raw.asTransfer()
        transfer.transferType == BANK_CHARGES
        transfer.balance == 300000
        transfer.amount == -2000

    }

    def "should convert version 2 of bank_charges string to raw"() {
        given:
        def converter = new BzWbkTransferRawConverter("0123")
        String csvLine = "03-08-2017|04-08-2017|Opł. za przelew ELIXIR - BZWBK24 od 1-06-2017 do 30-06-2017 szt. 2|||-1,00|300,00|10|"
        when:
        def raw = converter.convert(csvLine)
        then:
        raw != null
        def transfer = raw.asTransfer()
        transfer.transferType == BANK_CHARGES
        transfer.balance == 300000
        transfer.amount == -1000

    }

    def "should convert version 3 of bank_charges string to raw"() {
        given:
        def converter = new BzWbkTransferRawConverter("0123")
        String csvLine = "03-08-2017|04-08-2017|OPŁATA ZA WYPŁATĘ CASH BACK 444444******4444|||-1,50|300,00|10|"
        when:
        def raw = converter.convert(csvLine)
        then:
        raw != null
        def transfer = raw.asTransfer()
        transfer.transferType == BANK_CHARGES
        transfer.balance == 300000
        transfer.amount == -1500

    }

    def "should convert version 4 of bank_charges string to raw"() {
        given:
        def converter = new BzWbkTransferRawConverter("0123")
        String csvLine = "03-08-2017|04-08-2017|Opłata za Przelew24 od 1-05-2017|||-0,50|300,00|10|"
        when:
        def raw = converter.convert(csvLine)
        then:
        raw != null
        def transfer = raw.asTransfer()
        transfer.transferType == BANK_CHARGES
        transfer.balance == 300000
        transfer.amount == -500

    }

    def "should convert version 5 of bank_charges string to raw"() {
        given:
        def converter = new BzWbkTransferRawConverter("0123")
        String csvLine = "03-08-2017|04-08-2017|Opłata za przelew BlueCash - BZWBK24|||-5,00|300,00|10|"
        when:
        def raw = converter.convert(csvLine)
        then:
        raw != null
        def transfer = raw.asTransfer()
        transfer.transferType == BANK_CHARGES
        transfer.balance == 300000
        transfer.amount == -5000

    }

    def "should convert version 6 of bank_charges string to raw"() {
        given:
        def converter = new BzWbkTransferRawConverter("0123")
        String csvLine = "03-08-2017|04-08-2017|za wypłatę z bankomatu 444444******4444|||-9,00|300,00|10|"
        when:
        def raw = converter.convert(csvLine)
        then:
        raw != null
        def transfer = raw.asTransfer()
        transfer.transferType == BANK_CHARGES
        transfer.balance == 300000
        transfer.amount == -9000

    }

    def "should convert version 7 of bank_charges string to raw"() {
        given:
        def converter = new BzWbkTransferRawConverter("0123")
        String csvLine = "03-08-2017|04-08-2017|Sprawdzenie dostępnych środków444444******4444|||-2,00|300,00|10|"
        when:
        def raw = converter.convert(csvLine)
        then:
        raw != null
        def transfer = raw.asTransfer()
        transfer.transferType == BANK_CHARGES
        transfer.balance == 300000
        transfer.amount == -2000

    }

    def "should convert version 8 of bank_charges string to raw"() {
        given:
        def converter = new BzWbkTransferRawConverter("0123")
        String csvLine = "03-08-2017|04-08-2017|Pobrane odsetki od salda ujemn|||-0,04|300,00|10|"
        when:
        def raw = converter.convert(csvLine)
        then:
        raw != null
        def transfer = raw.asTransfer()
        transfer.transferType == BANK_CHARGES
        transfer.balance == 300000
        transfer.amount == -40

    }

    def "should convert version 9 of bank_charges string to raw"() {
        given:
        def converter = new BzWbkTransferRawConverter("0123")
        String csvLine = "03-08-2017|04-08-2017|Opłata miesięczna za kartę 444444******4444|||-4,00|300,00|10|"
        when:
        def raw = converter.convert(csvLine)
        then:
        raw != null
        def transfer = raw.asTransfer()
        transfer.transferType == BANK_CHARGES
        transfer.balance == 300000
        transfer.amount == -4000

    }

    def "should convert version 10 of bank_charges string to raw"() {
        given:
        def converter = new BzWbkTransferRawConverter("0123")
        String csvLine = "03-08-2017|04-08-2017|WYPŁATA Z BANKOMATU KARTĄ 444444******4444|||-5,00|300,00|10|"
        when:
        def raw = converter.convert(csvLine)
        then:
        raw != null
        def transfer = raw.asTransfer()
        transfer.transferType == BANK_CHARGES
        transfer.balance == 300000
        transfer.amount == -5000

    }

}
