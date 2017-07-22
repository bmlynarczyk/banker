package it.introsoft.banker.repository;

import it.introsoft.banker.model.raw.TransferType;
import it.introsoft.banker.view.AccountReportTransfer;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TransferRepositoryCustom {

    long setCategoryByDescriptionStartingWith(String category, String descriptor);

    long setCategoryByDescriptionEndingWith(String category, String descriptor);

    List<AccountReportTransfer> getByPeriod(String account, Date start, Date stop);

    Map<TransferType, Long> getAmountSumByTransferType(String account, Date start, Date stop);

    Map<TransferType, Long> getTransferCountByTransferType(String account, Date start, Date stop);
}
