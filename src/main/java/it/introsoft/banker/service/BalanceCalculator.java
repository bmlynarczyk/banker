package it.introsoft.banker.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import it.introsoft.banker.repository.QTransfer;
import it.introsoft.banker.repository.Transfer;
import it.introsoft.banker.repository.TransferRepository;
import lombok.extern.slf4j.Slf4j;

import static com.google.common.collect.Iterables.getFirst;

@Slf4j
public class BalanceCalculator {

    private final TransferRepository transferRepository;

    private final QTransfer qtransfer = QTransfer.transfer;

    public BalanceCalculator(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    public Long calculate(Transfer transfer) {
        log.debug(transfer.toString());
        if (transfer.getBalance() != null)
            return transfer.getBalance();
        else
            return getBalanceAndUpdateTransfers(transfer);
    }

    private long getBalanceAndUpdateTransfers(Transfer transfer) {
        Long balance = getBalance(transfer);
        transferRepository.updateBalanceInTodayTransfers(transfer);
        transferRepository.updateBalanceInLaterThanTodayTransfers(transfer);
        return balance;
    }

    private long getBalance(Transfer transfer) {
        Transfer previousTodayTransfer = getPreviousTodayTransfer(transfer);
        if (previousTodayTransfer != null)
            return previousTodayTransfer.getBalance() + transfer.getAmount();
        Transfer previousBeforeTodayTransfer = getPreviousBeforeTodayTransfer(transfer);
        if (previousBeforeTodayTransfer != null)
            return previousBeforeTodayTransfer.getBalance() + transfer.getAmount();
        else
            return transfer.getAmount();
    }

    private Transfer getPreviousTodayTransfer(Transfer transfer) {
        BooleanExpression currentDatePredicate = qtransfer.account.eq(transfer.getAccount())
                .and(qtransfer.date.eq(transfer.getDate()))
                .and(qtransfer.dateTransferNumber.lt(transfer.getDateTransferNumber()));
        return getFirst(transferRepository.findAll(currentDatePredicate, qtransfer.dateTransferNumber.desc()), null);
    }

    private Transfer getPreviousBeforeTodayTransfer(Transfer transfer) {
        BooleanExpression currentDatePredicate = qtransfer.account.eq(transfer.getAccount())
                .and(qtransfer.date.lt(transfer.getDate()));
        return getFirst(transferRepository.findAll(currentDatePredicate, qtransfer.date.desc(), qtransfer.dateTransferNumber.desc()), null);
    }
}
