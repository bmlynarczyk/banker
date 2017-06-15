package it.introsoft.banker.repository;

import it.introsoft.banker.view.AccountReportTransfer;
import it.introsoft.banker.model.transfer.Transfer;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TransferRepositoryCustom {

    void updateBalanceInLaterThanTodayTransfers(Transfer transfer);

    void updateBalanceInTodayTransfers(Transfer transfer);

    List<AccountReportTransfer> getByPeriod(Date start, Date stop);

    Map<String, Long> getAmountSumByTransferType(Date start, Date stop);

}
