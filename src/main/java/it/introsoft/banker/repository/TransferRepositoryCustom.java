package it.introsoft.banker.repository;

import it.introsoft.banker.view.AccountReportTransfer;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TransferRepositoryCustom {

    void updateBalanceInLaterThanTodayTransfers(Transfer transfer);

    void updateBalanceInTodayTransfers(Transfer transfer);

    List<AccountReportTransfer> getByPeriod(String account, Date start, Date stop);

    Map<String, Long> getAmountSumByTransferType(String account, Date start, Date stop);

    Map<String, Long> getTransferCountByTransferType(String account, Date start, Date stop);
}
