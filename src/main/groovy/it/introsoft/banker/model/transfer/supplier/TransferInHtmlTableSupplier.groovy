package it.introsoft.banker.model.transfer.supplier

import it.introsoft.banker.model.transfer.Transfer
import it.introsoft.banker.model.transfer.raw.TransferRaw
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.springframework.core.convert.converter.Converter

import java.util.function.Supplier

class TransferInHtmlTableSupplier implements Supplier<List<Transfer>> {

    private File file
    private Converter<Elements, List<TransferRaw>> converter

    TransferInHtmlTableSupplier(File file, Converter<Document, List<TransferRaw>> converter) {
        this.file = file
        this.converter = converter
    }

    @Override
    List<Transfer> get() {
        Document document = Jsoup.parse(file, 'utf-8')
        return converter.convert(document).collect { it.asTransfer() }
    }

}