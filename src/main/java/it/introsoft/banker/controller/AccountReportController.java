package it.introsoft.banker.controller;

import it.introsoft.banker.repository.AccountRepository;
import it.introsoft.banker.repository.TransferRepository;
import it.introsoft.banker.view.AccountReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/accounts/reports/")
@CrossOrigin
public class AccountReportController {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    @Autowired
    public AccountReportController(AccountRepository accountRepository, TransferRepository transferRepository) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }


    @GetMapping(value = "/{accountNumber}")
    public AccountReport getAccountReport(@PathVariable String accountNumber,
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date periodStart,
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date periodStop) {
        return new AccountReport(accountNumber, periodStart, periodStop, accountRepository, transferRepository);
    }

}
