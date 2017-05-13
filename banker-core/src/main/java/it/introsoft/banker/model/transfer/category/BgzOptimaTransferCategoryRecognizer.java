package it.introsoft.banker.model.transfer.category;

public class BgzOptimaTransferCategoryRecognizer implements TransferCategoryRecognizer {
    @Override
    public TransferCategory recognize(String description, String amount) {
        return TransferCategory.SAVINGS;
    }

}
