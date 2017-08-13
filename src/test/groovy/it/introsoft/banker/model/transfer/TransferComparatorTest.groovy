package it.introsoft.banker.model.transfer

import it.introsoft.banker.model.jpa.Transfer
import it.introsoft.banker.service.supplier.TransferComparator
import spock.lang.Specification

import java.time.format.DateTimeFormatter

import static java.time.LocalDate.parse

class TransferComparatorTest extends Specification {

    DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(Locale.getDefault())

    def transfersWithDateTransferNumber = [
            Transfer.builder().date(parse("21/12/2012", df)).
                    balance(null)
                    .dateTransferNumber(3).build(),
            Transfer.builder().date(parse("21/12/2012", df)).
                    balance(null)
                    .dateTransferNumber(2).build(),
            Transfer.builder().date(parse("21/12/2012", df)).
                    balance(null)
                    .dateTransferNumber(1).build(),
            Transfer.builder().date(parse("20/12/2012", df)).
                    balance(null)
                    .dateTransferNumber(2).build(),
            Transfer.builder().date(parse("20/12/2012", df)).
                    balance(null)
                    .dateTransferNumber(1).build(),
            Transfer.builder().date(parse("19/12/2012", df)).
                    balance(null)
                    .dateTransferNumber(1).build()
    ]

    def transfersWithBalance = [
            Transfer.builder().date(parse("21/12/2012", df)).
                    balance(70)
                    .dateTransferNumber(null).build(),
            Transfer.builder().date(parse("21/12/2012", df)).
                    balance(60)
                    .dateTransferNumber(null).build(),
            Transfer.builder().date(parse("21/12/2012", df)).
                    balance(50)
                    .dateTransferNumber(null).build(),
            Transfer.builder().date(parse("20/12/2012", df)).
                    balance(40)
                    .dateTransferNumber(null).build(),
            Transfer.builder().date(parse("20/12/2012", df)).
                    balance(30)
                    .dateTransferNumber(null).build(),
            Transfer.builder().date(parse("19/12/2012", df)).
                    balance(20)
                    .dateTransferNumber(null).build()
    ]

    def transfersWithDateTransferNumberAndBalance = [
            Transfer.builder().date(parse("21/12/2012", df)).
                    balance(70)
                    .dateTransferNumber(3).build(),
            Transfer.builder().date(parse("21/12/2012", df)).
                    balance(90)
                    .dateTransferNumber(2).build(),
            Transfer.builder().date(parse("21/12/2012", df)).
                    balance(100)
                    .dateTransferNumber(1).build(),
            Transfer.builder().date(parse("20/12/2012", df)).
                    balance(40)
                    .dateTransferNumber(2).build(),
            Transfer.builder().date(parse("20/12/2012", df)).
                    balance(30)
                    .dateTransferNumber(1).build(),
            Transfer.builder().date(parse("19/12/2012", df)).
                    balance(20)
                    .dateTransferNumber(1).build()
    ]

    def transfersWithoutDateTransferNumberAndBalance = [
            Transfer.builder().date(parse("21/12/2012", df)).
                    balance(null)
                    .dateTransferNumber(null).build(),
            Transfer.builder().date(parse("21/12/2012", df)).
                    balance(null)
                    .dateTransferNumber(null).build()
    ]

    def 'should sort by date and transfer number'() {
        def copy = transfersWithDateTransferNumber.collect()
        when:
        Collections.sort(copy, new TransferComparator())
        then:
        copy == transfersWithDateTransferNumber.reverse()
    }

    def 'should always sort by date and transfer number'() {
        def copy = transfersWithDateTransferNumber.collect()
        Collections.shuffle(copy, new Random(System.nanoTime()))
        when:
        Collections.sort(copy, new TransferComparator())
        then:
        copy == transfersWithDateTransferNumber.reverse()
    }

    def 'should sort by date and balance'() {
        def copy = transfersWithBalance.collect()
        when:
        Collections.sort(copy, new TransferComparator())
        then:
        copy == transfersWithBalance.reverse()
    }

    def 'should sort by date in favor of transfer number when present is also balance'() {
        def copy = transfersWithDateTransferNumberAndBalance.collect()
        Collections.shuffle(copy)
        when:
        Collections.sort(copy, new TransferComparator())
        then:
        copy == transfersWithDateTransferNumberAndBalance.reverse()
    }

    def 'should thrown exception when sort is impossible'() {
        when:
        Collections.sort(transfersWithoutDateTransferNumberAndBalance, new TransferComparator())
        then:
        IllegalStateException ex = thrown()
        ex.message == 'sort is impossible'
    }

}