package it.introsoft.banker.model.transfer

import spock.lang.Specification

import java.text.SimpleDateFormat

class TransferComparatorTest extends Specification {

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy")

    def transfersWithDateTransferNumber = [
            new Transfer(
                    date: sdf.parse("21/12/2012"),
                    balance: null,
                    dateTransferNumber: 3
            ),
            new Transfer(
                    date: sdf.parse("21/12/2012"),
                    balance: null,
                    dateTransferNumber: 2
            ),
            new Transfer(
                    date: sdf.parse("21/12/2012"),
                    balance: null,
                    dateTransferNumber: 1
            ),
            new Transfer(
                    date: sdf.parse("20/12/2012"),
                    balance: null,
                    dateTransferNumber: 2
            ),
            new Transfer(
                    date: sdf.parse("20/12/2012"),
                    balance: null,
                    dateTransferNumber: 1
            ),
            new Transfer(
                    date: sdf.parse("19/12/2012"),
                    balance: null,
                    dateTransferNumber: 1
            )
    ]

    def transfersWithBalance = [
            new Transfer(
                    date: sdf.parse("21/12/2012"),
                    balance: 70,
                    dateTransferNumber: null
            ),
            new Transfer(
                    date: sdf.parse("21/12/2012"),
                    balance: 60,
                    dateTransferNumber: null
            ),
            new Transfer(
                    date: sdf.parse("21/12/2012"),
                    balance: 50,
                    dateTransferNumber: null
            ),
            new Transfer(
                    date: sdf.parse("20/12/2012"),
                    balance: 40,
                    dateTransferNumber: null
            ),
            new Transfer(
                    date: sdf.parse("20/12/2012"),
                    balance: 30,
                    dateTransferNumber: null
            ),
            new Transfer(
                    date: sdf.parse("19/12/2012"),
                    balance: 20,
                    dateTransferNumber: null
            )
    ]

    def transfersWithDateTransferNumberAndBalance = [
            new Transfer(
                    date: sdf.parse("21/12/2012"),
                    balance: 70,
                    dateTransferNumber: 3
            ),
            new Transfer(
                    date: sdf.parse("21/12/2012"),
                    balance: 90,
                    dateTransferNumber: 2
            ),
            new Transfer(
                    date: sdf.parse("21/12/2012"),
                    balance: 100,
                    dateTransferNumber: 1
            ),
            new Transfer(
                    date: sdf.parse("20/12/2012"),
                    balance: 40,
                    dateTransferNumber: 2
            ),
            new Transfer(
                    date: sdf.parse("20/12/2012"),
                    balance: 30,
                    dateTransferNumber: 1
            ),
            new Transfer(
                    date: sdf.parse("19/12/2012"),
                    balance: 20,
                    dateTransferNumber: 1
            )
    ]

    def transfersWithoutDateTransferNumberAndBalance = [
            new Transfer(
                    date: sdf.parse("21/12/2012"),
                    balance: null,
                    dateTransferNumber: null
            ),
            new Transfer(
                    date: sdf.parse("21/12/2012"),
                    balance: null,
                    dateTransferNumber: null
            )
    ]

    def 'should sort by date and transfer number'(){
        def copy = transfersWithDateTransferNumber.collect()
        when:
        Collections.sort(copy, new TransferComparator())
        then:
        copy == transfersWithDateTransferNumber.reverse()
    }

    def 'should always sort by date and transfer number'(){
        def copy = transfersWithDateTransferNumber.collect()
        Collections.shuffle(copy, new Random(System.nanoTime()))
        when:
        Collections.sort(copy, new TransferComparator())
        then:
        copy == transfersWithDateTransferNumber.reverse()
    }

    def 'should sort by date and balance'(){
        def copy = transfersWithBalance.collect()
        when:
        Collections.sort(copy, new TransferComparator())
        then:
        copy == transfersWithBalance.reverse()
    }

    def 'should always sort by date and balance'(){
        def copy = transfersWithBalance.collect()
        Collections.shuffle(copy, new Random(System.nanoTime()))
        when:
        Collections.sort(copy, new TransferComparator())
        then:
        copy == transfersWithBalance.reverse()
    }

    def 'should sort by date in favor of transfer number when present is also balance'(){
        def copy = transfersWithDateTransferNumberAndBalance.collect()
        Collections.shuffle(copy)
        when:
        Collections.sort(copy, new TransferComparator())
        then:
        copy == transfersWithDateTransferNumberAndBalance.reverse()
    }

    def 'should thrown exception when sort is impossible'(){
        when:
        Collections.sort(transfersWithoutDateTransferNumberAndBalance, new TransferComparator())
        then:
        IllegalStateException ex = thrown()
        ex.message == 'sort is impossible'
    }

}