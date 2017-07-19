package it.introsoft.banker.service.type;

import it.introsoft.banker.model.raw.TransferType;

public interface TransferTypeRecognizer {
    TransferType recognize(String describer, String amount);
}
