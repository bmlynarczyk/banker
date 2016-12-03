package it.introsoft.banker.model.transfer.category

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@Slf4j
@CompileStatic
class BgzOptimaTransferCategoryRecognizer implements TransferCategoryRecognizer {

    @Override
    TransferCategory recognize(String description, String amount) {
        return TransferCategory.SAVINGS
    }

}
