package it.introsoft.banker.controller;

import it.introsoft.banker.service.BalanceCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/accounts/balances")
@CrossOrigin
public class AccountsBalancesController {

    @Autowired
    private BalanceCalculator balanceCalculator;

    @PostMapping("/recalculate")
    public void recalculate(@RequestParam String accountNumber) {
        balanceCalculator.recalculateBalanceForAllTransfers(accountNumber);
    }

}