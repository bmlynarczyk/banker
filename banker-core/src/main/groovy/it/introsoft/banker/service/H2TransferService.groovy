package it.introsoft.banker.service

import com.google.common.base.Stopwatch
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import it.introsoft.banker.model.transfer.Transfer
import it.introsoft.banker.repository.h2.H2Transfer
import it.introsoft.banker.repository.h2.H2TransferRepository

import static it.introsoft.banker.repository.h2.QH2Transfer.h2Transfer

@Slf4j
@CompileStatic
class H2TransferService implements TransferService {

    private final H2TransferRepository transferRepository
    private final H2BalanceCalculator balanceCalculator

    H2TransferService(H2TransferRepository transferRepository) {
        this.transferRepository = transferRepository
        this.balanceCalculator = new H2BalanceCalculator(transferRepository)
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

    private H2Transfer getTransferFromMongo(Transfer transfer) {
        def stopwatch = Stopwatch.createStarted()

        def predicate = (
                h2Transfer.date.eq(transfer.date)
                        & h2Transfer.account.eq(transfer.account)
                        & h2Transfer.amount.eq(transfer.amount)
                        & h2Transfer.description.eq(transfer.description)
        )

        if (transfer.balance)
            predicate = predicate & h2Transfer.balance.eq(transfer.balance)

        if (transfer.dateTransferNumber)
            predicate = predicate & h2Transfer.dateTransferNumber.eq(transfer.dateTransferNumber)

        log.trace(predicate.toString())
        def mongoTransfer = transferRepository.findOne(predicate)
        log.debug('check existence of transfer in: {}', stopwatch)
        return mongoTransfer
    }

    private void saveInRepo(Transfer transfer) {
        transferRepository.save(new H2Transfer(
                date: transfer.date,
                dateTransferNumber: transfer.dateTransferNumber ?: getDateTransferNumber(transfer.date),
                description: transfer.description,
                amount: transfer.amount,
                balance: balanceCalculator.calculate(transfer),
                currency: transfer.currency,
                transferType: transfer.transferType,
                account: transfer.account,
                beneficiaryAccount: transfer.beneficiaryAccount,
                bank: transfer.bank,
                category: transfer.category,
        ))
    }

    private long getDateTransferNumber(Date date) {
        H2Transfer transfer = transferRepository.findAll(
                h2Transfer.date.eq(date),
                h2Transfer.dateTransferNumber.desc()
        )[0]
        return (transfer ? transfer.getDateTransferNumber() : 0L) + 1
    }

    @Override
    void deleteAll() {
        transferRepository.deleteAll()
    }

}
