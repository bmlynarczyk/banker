package it.introsoft.banker.service.collector;

import it.introsoft.banker.repository.Transfer;

public class EmptyCardPaymentDescriptorCollector implements CardPaymentDescriptorCollector {

    @Override
    public void accept(Transfer transfer) {
    }

}
