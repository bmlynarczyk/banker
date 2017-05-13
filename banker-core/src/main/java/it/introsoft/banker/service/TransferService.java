package it.introsoft.banker.service;

import it.introsoft.banker.model.transfer.Transfer;

public interface TransferService {
    Result save(Transfer transfer);

    void deleteAll();
}
