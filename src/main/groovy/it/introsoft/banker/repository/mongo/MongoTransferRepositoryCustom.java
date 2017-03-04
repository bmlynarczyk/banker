package it.introsoft.banker.repository.mongo;

import it.introsoft.banker.model.transfer.Transfer;

public interface MongoTransferRepositoryCustom {
    void updateBalanceInLaterThanTodayTransfers(Transfer transfer);

    void updateBalanceInTodayTransfers(Transfer transfer);
}
