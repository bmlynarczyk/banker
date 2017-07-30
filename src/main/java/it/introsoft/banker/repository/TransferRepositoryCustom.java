package it.introsoft.banker.repository;

import it.introsoft.banker.model.raw.Bank;
import it.introsoft.banker.model.raw.TransferType;
import it.introsoft.banker.view.AccountReportTransfer;
import it.introsoft.banker.view.CategorySum;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TransferRepositoryCustom {

    long setCategoryByDescriptionStartingWith(String category, String descriptor, TransferType transferType, Bank bank);

    long setCategoryByDescriptionEndingWith(String category, String descriptor, TransferType transferType, Bank bank);

    long setCategoryByBeneficiary(String category, String beneficiary, TransferType transferType, Bank bank);

    long setCategoryByPayee(String category, String payee, TransferType transferType, Bank bank);

    List<AccountReportTransfer> getByPeriod(String account, Date start, Date stop);

    List<CategorySum> getSumByCategories(Date start, Date stop);

    List<CategorySum> getSumByCategories(String account, Date start, Date stop);

    Map<TransferType, Long> getAmountSumByTransferType(String account, Date start, Date stop);

    Map<TransferType, Long> getTransferCountByTransferType(String account, Date start, Date stop);
}
