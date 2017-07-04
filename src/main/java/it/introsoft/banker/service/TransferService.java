package it.introsoft.banker.service;

import it.introsoft.banker.repository.Transfer;

public interface TransferService {
    Result save(Transfer transfer);
}
