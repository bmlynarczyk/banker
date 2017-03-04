package it.introsoft.banker.model

import groovy.transform.CompileStatic
import it.introsoft.banker.model.transfer.category.*
import it.introsoft.banker.model.transfer.type.*

@CompileStatic
enum Bank {

    BGZ_OPTIMA('bgzoptima'){

        @Override
        TransferTypeRecognizer typeRecognizer() {
            return new BgzOptimaTransferTypeRecognizer()
        }

        @Override
        TransferCategoryRecognizer categoryRecognizer() {
            return new BgzOptimaTransferCategoryRecognizer()
        }

    },
    PKO_BP('pkobp'){

        @Override
        TransferTypeRecognizer typeRecognizer() {
            return new PkoBpTransferTypeRecognizer()
        }

        @Override
        TransferCategoryRecognizer categoryRecognizer() {
            return new PkoBpTransferCategoryRecognizer()
        }

    },
    M_BANK('mbank'){

        @Override
        TransferTypeRecognizer typeRecognizer() {
            return new MBankTransferTypeRecognizer()
        }

        @Override
        TransferCategoryRecognizer categoryRecognizer() {
            return new MBankTransferCategoryRecognizer()
        }
    },
    MILLENIUM('millenium'){

        @Override
        TransferTypeRecognizer typeRecognizer() {
            return new MilleniumTransferTypeRecognizer()
        }

        @Override
        TransferCategoryRecognizer categoryRecognizer() {
            return new MilleniumTransferCategoryRecognizer()
        }
    }

    String name

    Bank(String name) {
        this.name = name
    }

    abstract TransferTypeRecognizer typeRecognizer()

    abstract TransferCategoryRecognizer categoryRecognizer()

}
