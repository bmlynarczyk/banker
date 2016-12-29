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
        Transfer transfer = new Transfer(account: '123', balance: null, amount: 1000, date: new Date(), dateTransferNumber: 1)
        when:
        def balance = balanceCalculator.calculate(transfer)
        then:
        transferRepository.findAll(_, _) >> []
        balance == 1000L
    }

    def "balance is sum of previous today transfer balance and today amount when exists previous today transfer"() {
        given:
        Transfer transfer = new Transfer(account: '123', balance: null, amount: 1000, date: new Date(), dateTransferNumber: 1)
        when:
        def balance = balanceCalculator.calculate(transfer)
        then:
        1 * transferRepository.findAll(_, _) >> [new MongoTransfer(balance: 1000)]
        balance == 2000L
    }

    def "balance is sum of previous balance and today amount when exists previous transfer"() {
        given:
        Transfer transfer = new Transfer(account: '123', balance: null, amount: 1000, date: new Date(), dateTransferNumber: 1)
        when:
        def balance = balanceCalculator.calculate(transfer)
        then:
        2 * transferRepository.findAll(_, _) >>> [[], [new MongoTransfer(balance: 1000)]]
        balance == 2000L
    }

    def "should update balance in later transfers"() {
        given:
        Transfer transfer = new Transfer(account: '123', balance: null, amount: 1000, date: new Date(), dateTransferNumber: 1)
        when:
        balanceCalculator.calculate(transfer)
        then:
        transferRepository.findAll(_, _) >> []
        1 * transferRepository.updateBalanceInTodayTransfers(_)
        1 * transferRepository.updateBalanceInLaterThanTodayTransfers(_)
    }

}
