package it.introsoft.banker

import it.introsoft.banker.model.transfer.Transfer
import it.introsoft.banker.service.TransferService
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

import java.util.function.Supplier

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
		def transfers = transfersSupplier.get()
		switch (operation) {
			case 'save':
				save(transfers)
				break
			case 'delete':
				transferService.deleteAll()
				break
			default:
				throw new UnsupportedOperationException()
		}
	}

	void save(List<Transfer> transfers) {
		for (Transfer transfer : transfers) {
			transferService.save(transfer)
		}
	}

}



