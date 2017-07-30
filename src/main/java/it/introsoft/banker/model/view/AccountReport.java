package it.introsoft.banker.model.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.introsoft.banker.model.jpa.Account;
import it.introsoft.banker.model.jpa.Transfer;
import it.introsoft.banker.model.raw.Bank;
import it.introsoft.banker.model.raw.TransferType;
import it.introsoft.banker.repository.AccountRepository;
import it.introsoft.banker.repository.TransferRepository;
import it.introsoft.banker.service.converter.CategoriesReportFactory;
import lombok.Value;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Value
public class AccountReport {

    private final Date periodStart;
    private final Date periodStop;
    private final String accountNumber;
    private final Bank bank;
    private final Long periodFirstBalance;
    private final Date periodFirstBalanceDate;
    private final Map<TransferType, Long> amountSumByTransferType;
    private final Map<TransferType, Long> transferCountByTransferType;
    private final Long periodLastBalance;
    private final Date periodLastBalanceDate;
    private final Long minBalance;
    private final Long maxBalance;
    private final List<AccountReportTransfer> transfers;
    private final CategoriesReport categoriesReport;

    @JsonIgnore
    private final AccountRepository accountRepository;

    @JsonIgnore
    private final TransferRepository transferRepository;

    public AccountReport(String accountNumber, Date periodStart, Date periodStop,
                         AccountRepository accountRepository, TransferRepository transferRepository,
                         CategoriesReportFactory categoriesReportFactory) {
        if (periodStart.after(periodStop))
            throw new IllegalArgumentException("period start is after stop");

        this.periodStart = periodStart;
        this.periodStop = periodStop;
        this.accountNumber = accountNumber;

        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;

        Account account = getAccount();
        this.bank = account.getBank();

        Transfer firstTransfer = getFirstTransfer();
        this.periodFirstBalance = firstTransfer.getBalance();
        this.periodFirstBalanceDate = firstTransfer.getDate();

        this.amountSumByTransferType = transferRepository.getAmountSumByTransferType(accountNumber, periodStart, periodStop);
        this.transferCountByTransferType = transferRepository.getTransferCountByTransferType(accountNumber, periodStart, periodStop);

        Transfer lastTransfer = getLastTransfer();
        this.periodLastBalance = lastTransfer.getBalance();
        this.periodLastBalanceDate = lastTransfer.getDate();

        this.minBalance = getMinBalance();
        this.maxBalance = getMaxBalance();

        this.transfers = getTransfersByPeriod();
        this.categoriesReport = categoriesReportFactory.convert(transferRepository.getSumByCategories(accountNumber, periodStart, periodStop));
    }

    private Account getAccount() {
        return accountRepository.findByNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("account doesn't exist"));
    }

    private Transfer getFirstTransfer() {
        return transferRepository.findFirstByAccountAndDateBetweenOrderByDateAscDateTransferNumberAsc(accountNumber, periodStart, periodStop)
                .orElseThrow(() -> new IllegalStateException("account hasn't transfers in given period"));
    }

    private Transfer getLastTransfer() {
        return transferRepository.findFirstByAccountAndDateBetweenOrderByDateDescDateTransferNumberDesc(accountNumber, periodStart, periodStop)
                .orElseThrow(() -> new IllegalStateException("account hasn't transfers in given period"));
    }

    private Long getMinBalance() {
        return transferRepository.findFirstByAccountAndDateBetweenOrderByBalanceAsc(accountNumber, periodStart, periodStop)
                .orElseThrow(() -> new IllegalArgumentException("account hasn't transfers in given period"))
                .getBalance();
    }

    private Long getMaxBalance() {
        return transferRepository.findFirstByAccountAndDateBetweenOrderByBalanceDesc(accountNumber, periodStart, periodStop)
                .orElseThrow(() -> new IllegalArgumentException("account hasn't transfers in given period"))
                .getBalance();
    }

    private List<AccountReportTransfer> getTransfersByPeriod() {
        return transferRepository.getByPeriod(accountNumber, periodStart, periodStop);
    }

}
