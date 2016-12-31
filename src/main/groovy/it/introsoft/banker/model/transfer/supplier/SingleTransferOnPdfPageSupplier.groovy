package it.introsoft.banker.model.transfer.supplier

import groovy.transform.CompileStatic
import it.introsoft.banker.model.transfer.Transfer
import it.introsoft.banker.model.transfer.TransferComparator
import it.introsoft.banker.model.transfer.raw.TransferRaw
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.text.PDFTextStripperByArea
import org.springframework.core.convert.converter.Converter

import java.awt.*
import java.util.List
import java.util.function.Supplier

@CompileStatic
class SingleTransferOnPdfPageSupplier implements Supplier<Collection<Transfer>> {

    private File file
    private Rectangle rectangle
    private Converter<List<String>, TransferRaw> converter

    SingleTransferOnPdfPageSupplier(File file, Rectangle rectangle, Converter<List<String>, TransferRaw> converter) {
        this.file = file
        this.rectangle = rectangle
        this.converter = converter
    }

    @Override
    Collection<Transfer> get() {
        PDDocument document = null
        try {
            document = PDDocument.load(file)
            PDFTextStripperByArea stripper = new PDFTextStripperByArea()
            stripper.setSortByPosition(true)
            stripper.addRegion("class1", rectangle)
            return getTransfers(document, stripper)
        } finally {
            if (document != null) {
                document.close()
            }
        }
    }

    private Collection<Transfer> getTransfers(PDDocument document, PDFTextStripperByArea stripper) {
        def transfers = document.getPages().collect({ PDPage page ->
            stripper.extractRegions(page)
            def transferStrings = getTransferDataAsLines(stripper)
            return converter.convert(transferStrings).asTransfer()
        })
        Collections.sort(transfers, new TransferComparator())
        return transfers
    }

    private static List<String> getTransferDataAsLines(PDFTextStripperByArea stripper) {
        return stripper.getTextForRegion("class1").readLines()
    }

}