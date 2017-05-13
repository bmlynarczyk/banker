package it.introsoft.banker.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import it.introsoft.banker.model.transfer.Transfer;
import it.introsoft.banker.repository.H2Transfer;
import it.introsoft.banker.repository.TransferRepository;
import lombok.extern.slf4j.Slf4j;

import static com.google.common.collect.Iterables.getFirst;
import static it.introsoft.banker.repository.QH2Transfer.h2Transfer;

@Slf4j
public class BalanceCalculator {

    private final TransferRepository transferRepository;

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
        H2Transfer previousTodayTransfer = getPreviousTodayTransfer(transfer);
        if (previousTodayTransfer != null)
            return previousTodayTransfer.getBalance() + transfer.getAmount();
        H2Transfer previousBeforeTodayTransfer = getPreviousBeforeTodayTransfer(transfer);
        if (previousBeforeTodayTransfer != null)
            return previousBeforeTodayTransfer.getBalance() + transfer.getAmount();
        else
            return transfer.getAmount();
    }

    private H2Transfer getPreviousTodayTransfer(Transfer transfer) {
        BooleanExpression currentDatePredicate = h2Transfer.account.eq(transfer.getAccount())
                .and(h2Transfer.date.eq(transfer.getDate()))
                .and(h2Transfer.dateTransferNumber.lt(transfer.getDateTransferNumber()));
        return getFirst(transferRepository.findAll(currentDatePredicate, h2Transfer.dateTransferNumber.desc()), null);
    }

    private H2Transfer getPreviousBeforeTodayTransfer(Transfer transfer) {
        BooleanExpression currentDatePredicate = h2Transfer.account.eq(transfer.getAccount())
                .and(h2Transfer.date.lt(transfer.getDate()));
        return getFirst(transferRepository.findAll(currentDatePredicate, h2Transfer.date.desc(), h2Transfer.dateTransferNumber.desc()), null);
    }
}
