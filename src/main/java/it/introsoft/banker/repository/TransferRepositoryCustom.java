package it.introsoft.banker.repository;

import it.introsoft.banker.model.raw.Bank;
import it.introsoft.banker.model.raw.TransferType;
import it.introsoft.banker.model.view.AccountReportTransfer;
import it.introsoft.banker.model.view.CategorySum;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TransferRepositoryCustom {

    long setCategoryByDescriptionStartingWith(String category, String descriptor, TransferType transferType, Bank bank);

    long setCategoryByDescriptionEndingWith(String category, String descriptor, TransferType transferType, Bank bank);

    long setCategoryByBeneficiary(String category, String beneficiary, TransferType transferType, Bank bank);

    long setCategoryByPayee(String category, String payee, TransferType transferType, Bank bank);

    List<AccountReportTransfer> getByPeriod(String account, LocalDate start, LocalDate stop);

    List<CategorySum> getSumByCategoriesExcludingCategories(LocalDate start, LocalDate stop, List<String> excludedCategories);

    List<CategorySum> getSumByCategory(LocalDate start, LocalDate stop, String category);

    List<CategorySum> getSumByCategories(String account, LocalDate start, LocalDate stop);

    Map<TransferType, Long> getAmountSumByTransferType(String account, LocalDate start, LocalDate stop);

    Map<TransferType, Long> getTransferCountByTransferType(String account, LocalDate start, LocalDate stop);
}
