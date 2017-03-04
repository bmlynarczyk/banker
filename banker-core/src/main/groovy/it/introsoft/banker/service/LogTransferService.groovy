package it.introsoft.banker.service

import groovy.util.logging.Slf4j
import it.introsoft.banker.model.transfer.Transfer

@Slf4j
class LogTransferService implements TransferService {

    @Override
    Result save(Transfer transfer) {
        log.info(transfer.toString())
        return Result.SAVED
    }

    @Override
    void deleteAll() {
        log.info('deleteAll')
    }
}
