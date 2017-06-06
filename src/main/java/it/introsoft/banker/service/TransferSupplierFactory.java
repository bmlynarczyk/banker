package it.introsoft.banker.service;

import it.introsoft.banker.model.Bank;
import it.introsoft.banker.model.transfer.Transfer;
import it.introsoft.banker.model.transfer.raw.converter.BgzOptimaTransferRawConverter;
import it.introsoft.banker.model.transfer.raw.converter.MBankTransferRawConverter;
import it.introsoft.banker.model.transfer.raw.converter.MilleniumTransferRawConverter;
import it.introsoft.banker.model.transfer.raw.converter.PkoBpTransferRawConverter;
import it.introsoft.banker.model.transfer.supplier.MultiTransferOnPdfPageSupplier;
import it.introsoft.banker.model.transfer.supplier.SingleTransferOnPdfPageSupplier;
import it.introsoft.banker.model.transfer.supplier.TransferInHtmlTableSupplier;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.util.Collection;
import java.util.function.Supplier;

@Component
public class TransferSupplierFactory {

    public Supplier<Collection<Transfer>> getTransferSupplier(Bank bank, File file, String account) {
        switch (bank) {
            case BGZ_OPTIMA:
                return bgzOptimaTransfersSupplier(file, account);
            case PKO_BP:
                return pkobpTransfersSupplier(file, account);
            case M_BANK:
                return mbankTransfersSupplier(file, account);
            case MILLENIUM:
                return milleniumTransfersSupplier(file, account);
            default:
                throw new IllegalArgumentException("Unknown bank");
        }
    }

    private Supplier<Collection<Transfer>> bgzOptimaTransfersSupplier(File file, String account) {
        return new MultiTransferOnPdfPageSupplier(
                file,
                new Rectangle(10, 10, 590, 790),
                new BgzOptimaTransferRawConverter(account)
        );
    }

    private Supplier<Collection<Transfer>> pkobpTransfersSupplier(File file, String account) {
        return new TransferInHtmlTableSupplier(file, new PkoBpTransferRawConverter(account));
    }


    private Supplier<Collection<Transfer>> mbankTransfersSupplier(File file, String account) {
        return new SingleTransferOnPdfPageSupplier(
                file,
                new Rectangle(10, 10, 550, 790),
                new MBankTransferRawConverter(account)
        );
    }

    private Supplier<Collection<Transfer>> milleniumTransfersSupplier(File file, String account) {
        return new SingleTransferOnPdfPageSupplier(
                file,
                new Rectangle(10, 50, 550, 800),
                new MilleniumTransferRawConverter(account)
        );
    }

}
