package it.introsoft.banker.model.transfer.supplier;

import it.introsoft.banker.model.transfer.Transfer;
import it.introsoft.banker.model.transfer.raw.converter.BgzOptimaTransferRawConverter;
import it.introsoft.banker.model.transfer.raw.converter.MBankTransferRawConverter;
import it.introsoft.banker.model.transfer.raw.converter.MilleniumTransferRawConverter;
import it.introsoft.banker.model.transfer.raw.converter.PkoBpTransferRawConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

@Configuration
public class TransferSupplierConfiguration {

    @Bean("transfersSupplier")
    @ConditionalOnProperty(value = "bank", havingValue = "default", matchIfMissing = true)
    public Supplier<Collection<Transfer>> dummyTransfersSupplier() {
        return ArrayList::new;
    }

    @Bean("transfersSupplier")
    @ConditionalOnProperty(value = "bank", havingValue = "bgzoptima")
    public Supplier<Collection<Transfer>> bgzOptimaTransfersSupplier(
            @Value("${pathname.bgzoptima}") String pathname, @Value("${account.bgzoptima}") String account) {
        return new MultiTransferOnPdfPageSupplier(
                new File(pathname),
                new Rectangle(10, 10, 590, 790),
                new BgzOptimaTransferRawConverter(account)
        );
    }

    @Bean("transfersSupplier")
    @ConditionalOnProperty(value = "bank", havingValue = "pkobp")
    public Supplier<Collection<Transfer>> pkobpTransfersSupplier(
            @Value("${pathname.pkobp}") String pathname, @Value("${account.pkobp}") String account) {
        return new TransferInHtmlTableSupplier(new File(pathname), new PkoBpTransferRawConverter(account));
    }

    @Bean("transfersSupplier")
    @ConditionalOnProperty(value = "bank", havingValue = "mbank")
    public Supplier<Collection<Transfer>> mbankTransfersSupplier(
            @Value("${pathname.mbank}") String pathname, @Value("${account.mbank}") String account) {
        return new SingleTransferOnPdfPageSupplier(
                new File(pathname),
                new Rectangle(10, 10, 550, 790),
                new MBankTransferRawConverter(account)
        );
    }

    @Bean("transfersSupplier")
    @ConditionalOnProperty(value = "bank", havingValue = "millenium")
    public Supplier<Collection<Transfer>> milleniumTransfersSupplier(
            @Value("${pathname.millenium}") String pathname, @Value("${account.millenium}") String account) {
        return new SingleTransferOnPdfPageSupplier(
                new File(pathname),
                new Rectangle(10, 50, 550, 800),
                new MilleniumTransferRawConverter(account)
        );
    }

}
