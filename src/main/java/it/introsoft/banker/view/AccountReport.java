package it.introsoft.banker.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.introsoft.banker.repository.Account;
import it.introsoft.banker.repository.AccountRepository;
import it.introsoft.banker.repository.TransferRepository;
import lombok.Value;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Value
public class AccountReport {

    private final Date periodStart;
    private final Date periodStop;
    private final String accountNumber;
    private final String bank;
    private final Long periodFirstBalance;
    private final Map<String, Long> amountSumByTransferType;
    private final Long periodLastBalance;
    private final List<AccountReportTransfer> transfers;

    @JsonIgnore
    private final AccountRepository accountRepository;

    @JsonIgnore
    private final TransferRepository transferRepository;

    public AccountReport(String accountNumber, Date periodStart, Date periodStop,
                         AccountRepository accountRepository, TransferRepository transferRepository) {

        this.periodStart = periodStart;
        this.periodStop = periodStop;
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;

        Account account = getAccount(accountNumber);

        this.accountNumber = account.getNumber();
        this.bank = account.getBank();

        this.periodFirstBalance = getFirstBalance(accountNumber);
        this.amountSumByTransferType = transferRepository.getAmountSumByTransferType(periodStart, periodStop);
        this.periodLastBalance = getLastBalance(accountNumber);

        this.transfers = getTransfersByPeriod();

    }

    private Account getAccount(String accountNumber) {
        return accountRepository.findByNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("account doesn't exist"));
    }

    private Long getFirstBalance(String accountNumber) {
        return transferRepository.findFirstByAccountAndDateBetweenOrderByDateAscDateTransferNumberAsc(accountNumber, periodStart, periodStop)
                .orElseThrow(() -> new IllegalStateException("account hasn't transfers in given period"))
                .getBalance();
    }

    private List<AccountReportTransfer> getTransfersByPeriod() {
        return transferRepository.getByPeriod(periodStart, periodStop);
    }

    private Long getLastBalance(String accountNumber) {
        return transferRepository.findFirstByAccountAndDateBetweenOrderByDateDescDateTransferNumberDesc(accountNumber, periodStart, periodStop)
                .orElseThrow(() -> new IllegalStateException("account hasn't transfers in given period"))
                .getBalance();
    }

}
