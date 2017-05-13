package it.introsoft.banker;

import com.google.common.base.Stopwatch;
import it.introsoft.banker.model.transfer.Transfer;
import it.introsoft.banker.service.Result;
import it.introsoft.banker.service.TransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

@Slf4j
@SpringBootApplication
public class BankerApplication implements CommandLineRunner {

    @Value("${operation}")
    String operation;

    @Autowired
    TransferService transferService;

    @Autowired
    Supplier<Collection<Transfer>> transfersSupplier;

    public static void main(String[] args) {
        SpringApplication.run(BankerApplication.class, args);
    }

    public void run(String... args) {
        switch (operation) {
            case "save":
                save(transfersSupplier.get());
                break;
            case "delete":
                transferService.deleteAll();
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private void save(Collection<Transfer> transfers) {
        Stopwatch stopwatch = Stopwatch.createStarted();

        List<Result> results = transfers.stream()
                .map(transfer -> transferService.save(transfer))
                .collect(toList());

        log.info("{} transfers saved", results.stream().filter(it -> (it == Result.SAVED)).count());
        log.info("{} transfers existing", results.stream().filter(it -> (it == Result.EXISTING)).count());
        log.info("operation executed for {} transfers in: {}", transfers.size(), stopwatch);
    }

}