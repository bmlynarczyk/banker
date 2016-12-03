package it.introsoft.banker.service

import it.introsoft.banker.model.transfer.Transfer
import it.introsoft.banker.repository.MongoTransferRepository

class MongoTransferService implements TransferService {

    private final MongoTransferRepository mongoTransferRepository

    MongoTransferService(MongoTransferRepository mongoTransferRepository) {
        this.mongoTransferRepository = mongoTransferRepository
    }

    @Override
    void save(Transfer transfer) {
        mongoTransferRepository.save(transfer)
    }

    @Override
    void deleteAll() {
        mongoTransferRepository.deleteAll()
    }
}
