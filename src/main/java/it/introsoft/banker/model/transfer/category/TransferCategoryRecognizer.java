package it.introsoft.banker.model.transfer.category;

public interface TransferCategoryRecognizer {
    TransferCategory recognize(String description, String amount);
}
