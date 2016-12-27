package it.introsoft.banker.service

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import it.introsoft.banker.model.transfer.Transfer
import it.introsoft.banker.repository.MongoTransfer
import it.introsoft.banker.repository.MongoTransferRepository

import static it.introsoft.banker.repository.QMongoTransfer.mongoTransfer

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
        else {
            if (existsTransfersForAccount(transfer)) {
                Long balance = getBalance(transfer)
                if (mongoTransferRepository.exists(mongoTransfer.account.eq(transfer.account) & mongoTransfer.date.gt(transfer.date))){
                    mongoTransferRepository.updateBalanceInLaterTransfers(transfer.account, transfer.amount, transfer.date)
                }
                return balance
            } else
                return transfer.amount
        }
    }

    private boolean existsTransfersForAccount(Transfer transfer) {
        return mongoTransferRepository.exists(mongoTransfer.account.eq(transfer.account))
    }

    private long getBalance(Transfer transfer) {
        MongoTransfer current = getLastTransferFromAccountByDate(transfer.account, transfer.date)
        if (current)
            return current.balance + transfer.amount
        else {
            MongoTransfer nextTransfer = getNextTransferFromAccount(transfer.account, transfer.date)
            if (nextTransfer)
                return nextTransfer.balance + transfer.amount
            else
                return getPreviousTransferFromAccount(transfer.account, transfer.date).balance + transfer.amount
        }
    }

    private MongoTransfer getLastTransferFromAccountByDate(String account, Date date) {
        def currentDatePredicate = mongoTransfer.account.eq(account) & mongoTransfer.date.eq(date)
        return mongoTransferRepository.findAll(currentDatePredicate, mongoTransfer.dateTransferNumber.desc())[0]
    }

    private MongoTransfer getPreviousTransferFromAccount(String account, Date date) {
        def currentDatePredicate = mongoTransfer.account.eq(account) & mongoTransfer.date.lt(date)
        return mongoTransferRepository.findAll(currentDatePredicate, mongoTransfer.dateTransferNumber.desc())[0]
    }

    private MongoTransfer getNextTransferFromAccount(String account, Date date) {
        def currentDatePredicate = mongoTransfer.account.eq(account) & mongoTransfer.date.gt(date)
        return mongoTransferRepository.findAll(currentDatePredicate, mongoTransfer.dateTransferNumber.asc())[0]
    }

}