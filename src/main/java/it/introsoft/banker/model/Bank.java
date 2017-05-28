package it.introsoft.banker.model;

import it.introsoft.banker.model.transfer.category.*;
import it.introsoft.banker.model.transfer.type.*;

public enum Bank {

    BGZ_OPTIMA("bgzoptima") {
        @Override
        public TransferTypeRecognizer typeRecognizer() {
            return new BgzOptimaTransferTypeRecognizer();
        }

        @Override
        public TransferCategoryRecognizer categoryRecognizer() {
            return new BgzOptimaTransferCategoryRecognizer();
        }


    }, PKO_BP("pkobp") {
        @Override
        public TransferTypeRecognizer typeRecognizer() {
            return new PkoBpTransferTypeRecognizer();
        }

        @Override
        public TransferCategoryRecognizer categoryRecognizer() {
            return new PkoBpTransferCategoryRecognizer();
        }


    }, M_BANK("mbank") {
        @Override
        public TransferTypeRecognizer typeRecognizer() {
            return new MBankTransferTypeRecognizer();
        }

        @Override
        public TransferCategoryRecognizer categoryRecognizer() {
            return new MBankTransferCategoryRecognizer();
        }


    }, MILLENIUM("millenium") {
        @Override
        public TransferTypeRecognizer typeRecognizer() {
            return new MilleniumTransferTypeRecognizer();
        }

        @Override
        public TransferCategoryRecognizer categoryRecognizer() {
            return new MilleniumTransferCategoryRecognizer();
        }


    };

    private final String name;

    Bank(String name) {
        this.name = name;
    }

    public static Bank of(String name) {
        for (Bank value : values()) {
            if (value.getName().equals(name))
                return value;
        }
        throw new IllegalArgumentException("Unknown bank");
    }

    public abstract TransferTypeRecognizer typeRecognizer();

    public abstract TransferCategoryRecognizer categoryRecognizer();

    public String getName() {
        return name;
    }

}
