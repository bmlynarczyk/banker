package it.introsoft.banker.controller;

import com.google.common.base.Stopwatch;
import it.introsoft.banker.model.Bank;
import it.introsoft.banker.model.transfer.Transfer;
import it.introsoft.banker.service.Result;
import it.introsoft.banker.service.TransferService;
import it.introsoft.banker.service.TransferSupplierFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequestMapping("/import")
public class ImportController {

    @Autowired
    private TransferService transferService;

    @Autowired
    private TransferSupplierFactory transferSupplierFactory;

    @PostMapping
    public void importTransfers(@RequestParam String bankName, @RequestParam String filePath, @RequestParam String account) {
        Bank bank = Bank.of(bankName);
        Supplier<Collection<Transfer>> transferSupplier = transferSupplierFactory.getTransferSupplier(bank, filePath, account);
        save(transferSupplier.get());
    }

    private void save(Collection<Transfer> transfers) {
        Stopwatch stopwatch = Stopwatch.createStarted();

        List<Result> results = transfers.stream()
                .map(transferService::save)
                .collect(toList());

        log.info("{} transfers saved", results.stream().filter(it -> (it == Result.SAVED)).count());
        log.info("{} transfers existing", results.stream().filter(it -> (it == Result.EXISTING)).count());
        log.info("operation executed for {} transfers in: {}", transfers.size(), stopwatch);
    }

}