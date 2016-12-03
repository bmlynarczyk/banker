package it.introsoft.banker.service

import it.introsoft.banker.repository.MongoTransferRepository
import it.introsoft.banker.service.LogTransferService
import it.introsoft.banker.service.MongoTransferService
import it.introsoft.banker.service.TransferService
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
    @Profile('storage.log')
    TransferService logTransferService() {
        return new LogTransferService()
    }

}
