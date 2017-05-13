package it.introsoft.banker.repository;

import it.introsoft.banker.model.transfer.Transfer;

public interface TransferRepositoryCustom {

    void updateBalanceInLaterThanTodayTransfers(Transfer transfer);

    void updateBalanceInTodayTransfers(Transfer transfer);

}
