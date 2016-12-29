package it.introsoft.banker.model.transfer.supplier

import groovy.util.logging.Slf4j
import it.introsoft.banker.model.transfer.Transfer
import it.introsoft.banker.model.transfer.raw.converter.BgzOptimaTransferRawConverter
import it.introsoft.banker.model.transfer.raw.converter.MBankTransferRawConverter
import it.introsoft.banker.model.transfer.raw.converter.MilleniumTransferRawConverter
import it.introsoft.banker.model.transfer.raw.converter.PkoBpTransferRawConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import java.awt.*
import java.util.List
import java.util.function.Supplier

@Slf4j
@Configuration
class TransferSupplierConfiguration {

    @Bean('transfersSupplier')
    @ConditionalOnProperty(value = 'bank', havingValue = 'default', matchIfMissing = true)
    Supplier<Collection<Transfer>> dummyTransfersSupplier() {
        return new Supplier<Collection<Transfer>>() {
            List<Transfer> get() { return [] }
        }
    }

    @Bean('transfersSupplier')
    @ConditionalOnProperty(value = 'bank', havingValue = 'bgzoptima')
    Supplier<Collection<Transfer>> bgzOptimaTransfersSupplier(
            @Value('${pathname.bgzoptima}') String pathname, @Value('${account.bgzoptima}') String account) {
        return new MultiTransferOnPdfPageSupplier(
                new File(pathname),
                new Rectangle(10, 10, 590, 790),
                new BgzOptimaTransferRawConverter(account)
        )
    }

    @Bean('transfersSupplier')
    @ConditionalOnProperty(value = 'bank', havingValue = 'pkobp')
    Supplier<Collection<Transfer>> pkobpTransfersSupplier(
            @Value('${pathname.pkobp}') String pathname, @Value('${account.pkobp}') String account) {
        return new TransferInHtmlTableSupplier(new File(pathname), new PkoBpTransferRawConverter(account))
    }

    @Bean('transfersSupplier')
    @ConditionalOnProperty(value = 'bank', havingValue = 'mbank')
    Supplier<Collection<Transfer>> mbankTransfersSupplier(
            @Value('${pathname.mbank}') String pathname, @Value('${account.mbank}') String account) {
        return new SingleTransferOnPdfPageSupplier(
                new File(pathname),
                new Rectangle(10, 250, 550, 300),
                new MBankTransferRawConverter(account)
        )
    }

    @Bean('transfersSupplier')
    @ConditionalOnProperty(value = 'bank', havingValue = 'millenium')
    Supplier<Collection<Transfer>> milleniumTransfersSupplier(
            @Value('${pathname.millenium}') String pathname, @Value('${account.millenium}') String account) {
        return new SingleTransferOnPdfPageSupplier(
                new File(pathname),
                new Rectangle(10, 50, 550, 500),
                new MilleniumTransferRawConverter(account)
        )
    }

}