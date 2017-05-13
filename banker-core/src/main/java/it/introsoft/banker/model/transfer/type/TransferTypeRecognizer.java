package it.introsoft.banker.model.transfer.type;

public interface TransferTypeRecognizer {
    TransferType recognize(String describer, String amount);
}
