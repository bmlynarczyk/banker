package it.introsoft.banker.service

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import it.introsoft.banker.model.transfer.Transfer
import it.introsoft.banker.repository.mongo.MongoTransfer
import it.introsoft.banker.repository.mongo.MongoTransferRepository

import static it.introsoft.banker.repository.mongo.QMongoTransfer.mongoTransfer

@Slf4j
@CompileStatic
class MongoBalanceCalculator {

    private final MongoTransferRepository mongoTransferRepository

    MongoBalanceCalculator(MongoTransferRepository mongoTransferRepository) {
        this.mongoTransferRepository = mongoTransferRepository
    }

    Long calculate(Transfer transfer) {
        log.debug(transfer.toString())
        if (transfer.balance)
            return transfer.balance
        else
            return getBalanceAndUpdateTransfers(transfer)
    }

    private long getBalanceAndUpdateTransfers(Transfer transfer) {
        Long balance = getBalance(transfer)
        mongoTransferRepository.updateBalanceInTodayTransfers(transfer)
        mongoTransferRepository.updateBalanceInLaterThanTodayTransfers(transfer)
        return balance
    }

    private long getBalance(Transfer transfer) {
        MongoTransfer previousTodayTransfer = getPreviousTodayTransfer(transfer)
        if (previousTodayTransfer)
            return previousTodayTransfer.balance + transfer.amount
        MongoTransfer previousBeforeTodayTransfer = getPreviousBeforeTodayTransfer(transfer)
        if (previousBeforeTodayTransfer)
            return previousBeforeTodayTransfer.balance + transfer.amount
        else
            return transfer.amount
    }

    private MongoTransfer getPreviousTodayTransfer(Transfer transfer) {
        def currentDatePredicate = (mongoTransfer.account.eq(transfer.account)
                & mongoTransfer.date.eq(transfer.date)
                & mongoTransfer.dateTransferNumber.lt(transfer.dateTransferNumber))
        return mongoTransferRepository.findAll(currentDatePredicate, mongoTransfer.dateTransferNumber.desc())[0]
    }

    private MongoTransfer getPreviousBeforeTodayTransfer(Transfer transfer) {
        def currentDatePredicate = mongoTransfer.account.eq(transfer.account) & mongoTransfer.date.lt(transfer.date)
        return mongoTransferRepository.findAll(currentDatePredicate, mongoTransfer.date.desc(), mongoTransfer.dateTransferNumber.desc())[0]
    }

}