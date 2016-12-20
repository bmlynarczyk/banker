package it.introsoft.banker

import com.google.common.base.Stopwatch
import groovy.util.logging.Slf4j
import it.introsoft.banker.model.transfer.Transfer
import it.introsoft.banker.service.Result
import it.introsoft.banker.service.TransferService
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

import java.util.function.Supplier

@Slf4j
@CompileStatic
@SpringBootApplication
class BankerApplication implements CommandLineRunner {

	@Value('${operation}')
	String operation

	@Autowired
	TransferService transferService

	@Autowired
	Supplier<List<Transfer>> transfersSupplier

	static void main(String[] args) {
		SpringApplication.run BankerApplication, args
	}

	void run(String... args){
		switch (operation) {
			case 'save':
				save(transfersSupplier.get())
				break
			case 'delete':
				transferService.deleteAll()
				break
			default:
				throw new UnsupportedOperationException()
		}
	}

	void save(List<Transfer> transfers) {
		def stopwatch = Stopwatch.createStarted()
		List<Result> results = []
		for (Transfer transfer : transfers) {
			results << transferService.save(transfer)
		}
		log.info('{} transfers saved', results.findAll { (it == Result.SAVED) }.size())
		log.info('{} transfers existing', results.findAll { (it == Result.EXISTING) }.size())
		log.info('operation executed for {} transfers in: {}', transfers.size(), stopwatch)
	}

}