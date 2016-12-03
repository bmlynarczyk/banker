package it.introsoft.banker.model

import it.introsoft.banker.model.transfer.category.BgzOptimaTransferCategoryRecognizer
import it.introsoft.banker.model.transfer.category.MBankTransferCategoryRecognizer
import it.introsoft.banker.model.transfer.category.MilleniumTransferCategoryRecognizer
import it.introsoft.banker.model.transfer.category.PkoBpTransferCategoryRecognizer
import it.introsoft.banker.model.transfer.category.TransferCategoryRecognizer
import it.introsoft.banker.model.transfer.type.BgzOptimaTransferTypeRecognizer
import it.introsoft.banker.model.transfer.type.MBankTransferTypeRecognizer
import it.introsoft.banker.model.transfer.type.MilleniumTransferTypeRecognizer
import it.introsoft.banker.model.transfer.type.PkoBpTransferTypeRecognizer
import it.introsoft.banker.model.transfer.type.TransferTypeRecognizer
import groovy.transform.CompileStatic

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
