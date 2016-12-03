package it.introsoft.banker.model.transfer.type

interface TransferTypeRecognizer {
    TransferType recognize(String describer, String amount)
}
