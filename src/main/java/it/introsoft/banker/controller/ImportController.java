package it.introsoft.banker.controller;

import com.google.common.base.Stopwatch;
import com.google.common.eventbus.EventBus;
import it.introsoft.banker.model.transfer.Transfer;
import it.introsoft.banker.repository.Account;
import it.introsoft.banker.repository.AccountRepository;
import it.introsoft.banker.service.Result;
import it.introsoft.banker.service.TransferService;
import it.introsoft.banker.service.TransferSupplierFactory;
import it.introsoft.banker.service.UpdateAccountEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequestMapping("/import")
@CrossOrigin
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
    public void importTransfers(@RequestParam String filePath, @RequestParam String account) {
        Account accountFromDb = accountRepository.findByNumber(account).orElseThrow(ValidationException::new);
        File file = new File(filePath);
        if (!file.exists())
            throw new ValidationException("file does't exist");
        Supplier<Collection<Transfer>> transferSupplier = transferSupplierFactory.getTransferSupplier(file, accountFromDb);
        save(transferSupplier.get());
        eventBus.post(UpdateAccountEvent.builder().account(accountFromDb).build());
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