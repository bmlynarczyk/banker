package it.introsoft.banker.service

import it.introsoft.banker.model.transfer.Transfer
import it.introsoft.banker.repository.MongoTransfer
import it.introsoft.banker.repository.MongoTransferRepository
import spock.lang.Specification

import static it.introsoft.banker.repository.QMongoTransfer.mongoTransfer

class MongoBalanceCalculatorTest extends Specification {

    MongoTransferRepository transferRepository = Mock(MongoTransferRepository)

    MongoBalanceCalculator balanceCalculator = new MongoBalanceCalculator(transferRepository)

    def 'should return balance from transfer'() {
        given:
        Transfer transfer = new Transfer(balance: 1000)
        when:
        def balance = balanceCalculator.calculate(transfer)
        then:
        balance == 1000L
    }

    def "should return balance equals transfer amount when any transfer for this account doesn't exist"() {
        given:
        Transfer transfer = new Transfer(account: '123', balance: null, amount: 1000)
        when:
        def balance = balanceCalculator.calculate(transfer)
        then:
        transferRepository.exists(_) >> false
        balance == 1000L
    }

    def "transfer is from date before last transfer with balance and exists at least one transfer for transfer date"() {
        given:
        Transfer transfer = new Transfer(account: '123', balance: null, amount: 1000, date: new Date())
        when:
        def balance = balanceCalculator.calculate(transfer)
        then:
        transferRepository.exists(_) >>> [true, false]
        transferRepository.findAll(_, _) >> [new MongoTransfer(balance: 1000)]
        balance == 2000L
    }

    def "balance is sum of next transfer balance and today amount when doesn't exist any transfer for today"() {
        given:
        Transfer transfer = new Transfer(account: '123', balance: null, amount: 1000, date: new Date())
        when:
        def balance = balanceCalculator.calculate(transfer)
        then:
        transferRepository.exists(_) >>> [true, false]
        1 * transferRepository.findAll(_, mongoTransfer.dateTransferNumber.desc()) >> []
        1 * transferRepository.findAll(_, mongoTransfer.dateTransferNumber.asc()) >> [new MongoTransfer(balance: 1000)]
        balance == 2000L
    }

    def "balance is sum of previous balance and today amount when doesn't exist today and next transfer"() {
        given:
        Transfer transfer = new Transfer(account: '123', balance: null, amount: 1000, date: new Date())
        when:
        def balance = balanceCalculator.calculate(transfer)
        then:
        transferRepository.exists(_) >>> [true, false]
        2 * transferRepository.findAll(_, mongoTransfer.dateTransferNumber.desc()) >>> [[], [new MongoTransfer(balance: 1000)]]
        1 * transferRepository.findAll(_, mongoTransfer.dateTransferNumber.asc()) >> []
        balance == 2000L
    }

    def "should return correct balance when transfer date is after or in date of last transfer with balance"() {
        given:
        Transfer transfer = new Transfer(account: '123', balance: null, amount: 1000, date: new Date())
        when:
        balanceCalculator.calculate(transfer)
        then:
        transferRepository.exists(_) >>> [true, true]
        transferRepository.findAll(_, _) >> [new MongoTransfer(balance: 1000)]
        1 * transferRepository.updateBalanceInLaterTransfers(_, _, _)
    }

}
