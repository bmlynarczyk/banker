package it.introsoft.banker.model.transfer.supplier

import it.introsoft.banker.model.transfer.Transfer
import it.introsoft.banker.model.transfer.raw.TransferRaw
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.text.PDFTextStripperByArea
import org.springframework.core.convert.converter.Converter

import java.awt.*
import java.util.List
import java.util.function.Supplier

class MultiTransferOnPdfPageSupplier implements Supplier<List<Transfer>> {

    private File file
    private Rectangle rectangle
    private Converter<List<String>, List<TransferRaw>> converter

    MultiTransferOnPdfPageSupplier(File file, Rectangle rectangle, Converter<List<String>, List<TransferRaw>> converter) {
        this.file = file
        this.rectangle = rectangle
        this.converter = converter
    }

    @Override
    List<Transfer> get() {
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

    private List<Transfer> getTransfers(PDDocument document, PDFTextStripperByArea stripper) {
        return document.getPages().collectMany { PDPage page ->
            stripper.extractRegions(page)
            List<String> transferStrings = getTransferDataAsLines(stripper)
            return converter.convert(transferStrings).collect {it.asTransfer()}
        }
    }

    private static List<String> getTransferDataAsLines(PDFTextStripperByArea stripper) {
        return stripper.getTextForRegion("class1").readLines()
    }

}