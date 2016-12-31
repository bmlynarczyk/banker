package it.introsoft.banker.model.transfer.supplier

import groovy.transform.CompileStatic
import it.introsoft.banker.model.transfer.Transfer
import it.introsoft.banker.model.transfer.TransferComparator
import it.introsoft.banker.model.transfer.raw.TransferRaw
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.core.convert.converter.Converter

import java.util.function.Supplier

@CompileStatic
class TransferInHtmlTableSupplier implements Supplier<Collection<Transfer>> {

    private File file
    private Converter<Document, List<TransferRaw>> converter

    TransferInHtmlTableSupplier(File file, Converter<Document, List<TransferRaw>> converter) {
        this.file = file
        this.converter = converter
    }

    @Override
    Collection<Transfer> get() {
        Document document = Jsoup.parse(file, 'utf-8')
        def transfers = converter.convert(document).collect({ it.asTransfer() })
        Collections.sort(transfers, new TransferComparator())
        return transfers
    }

}