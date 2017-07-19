package it.introsoft.banker.service;

import it.introsoft.banker.model.jpa.Transfer;
import it.introsoft.banker.service.collector.CardPaymentDescriptorCollector;

public interface TransferService {
    Result save(Transfer transfer, CardPaymentDescriptorCollector cardPaymentDescriptorCollector);
}
