package it.introsoft.banker.model.transfer.category

interface TransferCategoryRecognizer {
    TransferCategory recognize(String description, String amount)
}
