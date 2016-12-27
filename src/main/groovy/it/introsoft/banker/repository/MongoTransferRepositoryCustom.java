package it.introsoft.banker.repository;

import java.util.Date;

public interface MongoTransferRepositoryCustom {
    void updateBalanceInLaterTransfers(String account, long amount, Date date);
}
