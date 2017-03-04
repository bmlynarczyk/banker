package it.introsoft.banker.service

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import it.introsoft.banker.model.transfer.Transfer
import it.introsoft.banker.repository.h2.H2Transfer
import it.introsoft.banker.repository.h2.H2TransferRepository

import static it.introsoft.banker.repository.h2.QH2Transfer.h2Transfer

@Slf4j
@CompileStatic
class H2BalanceCalculator {

    private final H2TransferRepository transferRepository

    H2BalanceCalculator(H2TransferRepository transferRepository) {
        this.transferRepository = transferRepository
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
        transferRepository.updateBalanceInTodayTransfers(transfer)
        transferRepository.updateBalanceInLaterThanTodayTransfers(transfer)
        return balance
    }

    private long getBalance(Transfer transfer) {
        def previousTodayTransfer = getPreviousTodayTransfer(transfer)
        if (previousTodayTransfer)
            return previousTodayTransfer.balance + transfer.amount
        def previousBeforeTodayTransfer = getPreviousBeforeTodayTransfer(transfer)
        if (previousBeforeTodayTransfer)
            return previousBeforeTodayTransfer.balance + transfer.amount
        else
            return transfer.amount
    }

    private H2Transfer getPreviousTodayTransfer(Transfer transfer) {
        def currentDatePredicate = (h2Transfer.account.eq(transfer.account)
                & h2Transfer.date.eq(transfer.date)
                & h2Transfer.dateTransferNumber.lt(transfer.dateTransferNumber))
        return transferRepository.findAll(currentDatePredicate, h2Transfer.dateTransferNumber.desc())[0]
    }

    private H2Transfer getPreviousBeforeTodayTransfer(Transfer transfer) {
        def currentDatePredicate = h2Transfer.account.eq(transfer.account) & h2Transfer.date.lt(transfer.date)
        return transferRepository.findAll(currentDatePredicate, h2Transfer.date.desc(), h2Transfer.dateTransferNumber.desc())[0]
    }

}