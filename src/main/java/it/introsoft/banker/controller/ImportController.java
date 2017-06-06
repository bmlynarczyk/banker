package it.introsoft.banker.controller;

import com.google.common.base.Stopwatch;
import com.google.common.eventbus.EventBus;
import it.introsoft.banker.model.Bank;
import it.introsoft.banker.model.transfer.Transfer;
import it.introsoft.banker.repository.AccountRepository;
import it.introsoft.banker.service.Result;
import it.introsoft.banker.service.TransferService;
import it.introsoft.banker.service.TransferSupplierFactory;
import it.introsoft.banker.service.UpdateAccountEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ValidationException;
import java.io.File;
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

    @Autowired
    private EventBus eventBus;

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping
    public void importTransfers(@RequestParam String bankName, @RequestParam String filePath, @RequestParam String account) {
        Bank bank = Bank.of(bankName);
        accountRepository.findByNumber(account).orElseThrow(ValidationException::new);
        File file = new File(filePath);
        if (!file.exists())
            throw new ValidationException("file does't exist");
        Supplier<Collection<Transfer>> transferSupplier = transferSupplierFactory.getTransferSupplier(bank, file, account);
        save(transferSupplier.get());
        eventBus.post(UpdateAccountEvent.builder().account(account).build());
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