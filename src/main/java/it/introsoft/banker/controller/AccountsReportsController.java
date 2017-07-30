package it.introsoft.banker.controller;

import it.introsoft.banker.repository.AccountRepository;
import it.introsoft.banker.repository.TransferRepository;
import it.introsoft.banker.model.view.AccountReport;
import it.introsoft.banker.service.converter.CategoriesReportFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/accounts/reports/")
@CrossOrigin
public class AccountsReportsController {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;
    private final CategoriesReportFactory categoriesReportFactory;

    @Autowired
    public AccountsReportsController(AccountRepository accountRepository,
                                     TransferRepository transferRepository,
                                     CategoriesReportFactory categoriesReportFactory) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
        this.categoriesReportFactory = categoriesReportFactory;
    }


    @GetMapping(value = "/{accountNumber}")
    public AccountReport getAccountReport(@PathVariable String accountNumber,
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date periodStart,
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date periodStop) {
        return new AccountReport(accountNumber, periodStart, periodStop, accountRepository, transferRepository, categoriesReportFactory);
    }

}
