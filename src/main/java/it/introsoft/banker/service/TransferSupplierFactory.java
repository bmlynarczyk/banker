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

    public Supplier<Collection<Transfer>> getTransferSupplier(Bank bank, String filePath, String account) {
        switch (bank) {
            case BGZ_OPTIMA:
                return bgzOptimaTransfersSupplier(filePath, account);
            case PKO_BP:
                return pkobpTransfersSupplier(filePath, account);
            case M_BANK:
                return mbankTransfersSupplier(filePath, account);
            case MILLENIUM:
                return milleniumTransfersSupplier(filePath, account);
            default:
                throw new IllegalArgumentException("Unknown bank");
        }
    }

    private Supplier<Collection<Transfer>> bgzOptimaTransfersSupplier(String pathname, String account) {
        return new MultiTransferOnPdfPageSupplier(
                new File(pathname),
                new Rectangle(10, 10, 590, 790),
                new BgzOptimaTransferRawConverter(account)
        );
    }

    private Supplier<Collection<Transfer>> pkobpTransfersSupplier(String pathname, String account) {
        return new TransferInHtmlTableSupplier(new File(pathname), new PkoBpTransferRawConverter(account));
    }


    private Supplier<Collection<Transfer>> mbankTransfersSupplier(String pathname, String account) {
        return new SingleTransferOnPdfPageSupplier(
                new File(pathname),
                new Rectangle(10, 10, 550, 790),
                new MBankTransferRawConverter(account)
        );
    }

    private Supplier<Collection<Transfer>> milleniumTransfersSupplier(String pathname, String account) {
        return new SingleTransferOnPdfPageSupplier(
                new File(pathname),
                new Rectangle(10, 50, 550, 800),
                new MilleniumTransferRawConverter(account)
        );
    }

}
