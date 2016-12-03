package it.introsoft.banker.service

import it.introsoft.banker.model.transfer.Transfer

interface TransferService {

    void save(Transfer transfer)

    void deleteAll()

}