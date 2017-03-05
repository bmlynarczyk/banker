package it.introsoft.banker.service

import com.google.common.base.Stopwatch
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import it.introsoft.banker.model.transfer.Transfer
import it.introsoft.banker.repository.mongo.MongoTransfer
import it.introsoft.banker.repository.mongo.MongoTransferRepository

import static it.introsoft.banker.repository.mongo.QMongoTransfer.mongoTransfer

@Slf4j
@CompileStatic
class MongoTransferService implements TransferService {

    private final MongoTransferRepository mongoTransferRepository
    private final MongoBalanceCalculator balanceCalculator

    MongoTransferService(MongoTransferRepository mongoTransferRepository) {
        this.mongoTransferRepository = mongoTransferRepository
        this.balanceCalculator = new MongoBalanceCalculator(mongoTransferRepository)
    }

    @Override
    Result save(Transfer transfer) {
        def stopwatch = Stopwatch.createStarted()
        def mongoTransfer = getTransferFromMongo(transfer)
        if (mongoTransfer) {
            log.info('transfer already exists with id: {}', mongoTransfer.id)
            return Result.EXISTING
        } else {
            saveInRepo(transfer)
            log.debug('transfer saved in: {}', stopwatch)
            return Result.SAVED
        }
    }

    private MongoTransfer getTransferFromMongo(Transfer transfer) {
        def stopwatch = Stopwatch.createStarted()

        def predicate = (
                mongoTransfer.date.eq(transfer.date)
                        & mongoTransfer.account.eq(transfer.account)
                        & mongoTransfer.amount.eq(transfer.amount)
                        & mongoTransfer.description.eq(transfer.description)
        )

        if (transfer.balance)
            predicate = predicate & mongoTransfer.balance.eq(transfer.balance)

        if (transfer.dateTransferNumber)
            predicate = predicate & mongoTransfer.dateTransferNumber.eq(transfer.dateTransferNumber)

        log.trace(predicate.toString())
        def mongoTransfer = mongoTransferRepository.findOne(predicate)
        log.debug('check existence of transfer in: {}', stopwatch)
        return mongoTransfer
    }

    private void saveInRepo(Transfer transfer) {
        mongoTransferRepository.save(new MongoTransfer(
                date: transfer.date,
                dateTransferNumber: transfer.dateTransferNumber ?: getDateTransferNumber(transfer.date),
                description: transfer.description,
                amount: transfer.amount,
                balance: balanceCalculator.calculate(transfer),
                currency: transfer.currency,
                transferType: transfer.transferType,
                account: transfer.account,
                beneficiaryName: transfer.beneficiaryName,
                beneficiaryAccount: transfer.beneficiaryAccount,
                beneficiaryAddress: transfer.beneficiaryAddress,
                bank: transfer.bank,
                category: transfer.category,
                tags: transfer.tags
        ))
    }

    private long getDateTransferNumber(Date date) {
        MongoTransfer transfer = mongoTransferRepository.findAll(
                mongoTransfer.date.eq(date),
                mongoTransfer.dateTransferNumber.desc()
        )[0]
        return (transfer ? transfer.getDateTransferNumber() : 0L) + 1
    }

    @Override
    void deleteAll() {
        mongoTransferRepository.deleteAll()
    }

}
