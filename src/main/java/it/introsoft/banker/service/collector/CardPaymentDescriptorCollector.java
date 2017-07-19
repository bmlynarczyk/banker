package it.introsoft.banker.service.collector;

import it.introsoft.banker.model.jpa.Transfer;

import java.util.function.Consumer;

public interface CardPaymentDescriptorCollector extends Consumer<Transfer> {
}
