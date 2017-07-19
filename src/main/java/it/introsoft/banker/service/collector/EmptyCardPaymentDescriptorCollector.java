package it.introsoft.banker.service.collector;

import it.introsoft.banker.model.jpa.Transfer;

public class EmptyCardPaymentDescriptorCollector implements CardPaymentDescriptorCollector {

    @Override
    public void accept(Transfer transfer) {
    }

}
