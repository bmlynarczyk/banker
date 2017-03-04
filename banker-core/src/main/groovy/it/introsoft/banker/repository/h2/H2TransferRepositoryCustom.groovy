package it.introsoft.banker.repository.h2;

import it.introsoft.banker.model.transfer.Transfer;

public interface H2TransferRepositoryCustom {
    void updateBalanceInLaterThanTodayTransfers(Transfer transfer);

    void updateBalanceInTodayTransfers(Transfer transfer);
}
