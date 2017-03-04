package it.introsoft.banker.service

import it.introsoft.banker.repository.h2.H2TransferRepository
import it.introsoft.banker.repository.mongo.MongoTransferRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
class ServiceConfiguration {

    @Bean('transferService')
    @Profile('storage.mongo')
    TransferService mongoTransferService(MongoTransferRepository mongoTransferRepository) {
        return new MongoTransferService(mongoTransferRepository)
    }

    @Bean('transferService')
    @Profile('storage.h2')
    TransferService h2TransferService(H2TransferRepository h2TransferRepository) {
        return new H2TransferService(h2TransferRepository)
    }

    @Bean('transferService')
    @Profile('storage.log')
    TransferService logTransferService() {
        return new LogTransferService()
    }

}
