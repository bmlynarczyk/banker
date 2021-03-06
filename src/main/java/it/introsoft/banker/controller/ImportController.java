package it.introsoft.banker.controller;

import com.google.common.base.Stopwatch;
import com.google.common.eventbus.EventBus;
import it.introsoft.banker.model.jpa.Account;
import it.introsoft.banker.model.jpa.Transfer;
import it.introsoft.banker.repository.AccountRepository;
import it.introsoft.banker.service.Result;
import it.introsoft.banker.service.TransferService;
import it.introsoft.banker.service.TransferSupplierFactory;
import it.introsoft.banker.service.collector.CardPaymentDescriptorCollector;
import it.introsoft.banker.service.collector.CardPaymentDescriptorCollectorFactory;
import it.introsoft.banker.service.event.UpdateAccountBalanceEvent;
import it.introsoft.banker.service.event.UpdateCategoryDescriptorsEvent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequestMapping("/api/import")
@CrossOrigin
public class ImportController {

    @Autowired
    private TransferService transferService;

    @Autowired
    private TransferSupplierFactory transferSupplierFactory;

    @Autowired
    private CardPaymentDescriptorCollectorFactory cardPaymentDescriptorCollectorFactory;

    @Autowired
    private EventBus eventBus;

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping
    @SneakyThrows
    public void importTransfers(@RequestParam String filePath,
                                @RequestParam String account,
                                @RequestParam(required = false) String categoriesMappingPath) {
        Account accountFromDb = accountRepository.findByNumber(account).orElseThrow(ValidationException::new);
        File file = new File(filePath);
        if (!file.exists())
            throw new ValidationException("file does't exist");
        Supplier<Collection<Transfer>> transferSupplier = transferSupplierFactory.getTransferSupplier(file, accountFromDb);
        save(accountFromDb, transferSupplier.get());
        eventBus.post(UpdateAccountBalanceEvent.builder().account(accountFromDb).build());
        if (nonNull(categoriesMappingPath) && !categoriesMappingPath.isEmpty()) {
            Sheet sheet = SpreadSheet.createFromFile(new File(categoriesMappingPath)).getSheet(0);
            eventBus.post(UpdateCategoryDescriptorsEvent.builder().bank(accountFromDb.getBank()).sheet(sheet).build());
        }
    }

    private void save(Account account, Collection<Transfer> transfers) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        CardPaymentDescriptorCollector cardPaymentDescriptorCollector = cardPaymentDescriptorCollectorFactory.get(account.getBank());
        List<Result> results = transfers.stream()
                .map(transfer -> transferService.save(transfer, cardPaymentDescriptorCollector))
                .collect(toList());

        log.info("{} transfers saved", results.stream().filter(it -> (it == Result.SAVED)).count());
        log.info("{} transfers existing", results.stream().filter(it -> (it == Result.EXISTING)).count());
        log.info("operation executed for {} transfers in: {}", transfers.size(), stopwatch);
    }

}