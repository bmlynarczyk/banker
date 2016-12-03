package it.introsoft.banker.service

import groovy.util.logging.Slf4j
import it.introsoft.banker.model.transfer.Transfer

@Slf4j
class LogTransferService implements TransferService {

    @Override
    void save(Transfer transfer) {
        log.info(transfer.toString())
    }

    @Override
    void deleteAll() {
        log.info('deleteAll')
    }
}
