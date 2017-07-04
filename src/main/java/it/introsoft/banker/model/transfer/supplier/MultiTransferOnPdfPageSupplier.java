package it.introsoft.banker.model.transfer.supplier;

import it.introsoft.banker.model.transfer.TransferComparator;
import it.introsoft.banker.model.transfer.raw.TransferRaw;
import it.introsoft.banker.repository.Transfer;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.core.convert.converter.Converter;

import java.awt.*;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import static com.google.common.base.Splitter.on;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.util.StreamUtils.createStreamFromIterator;

public class MultiTransferOnPdfPageSupplier implements Supplier<Collection<Transfer>> {

    private File file;
    private Rectangle rectangle;
    private Converter<List<String>, List<TransferRaw>> converter;

    public MultiTransferOnPdfPageSupplier(File file, Rectangle rectangle, Converter<List<String>, List<TransferRaw>> converter) {
        this.file = file;
        this.rectangle = rectangle;
        this.converter = converter;
    }

    private static List<String> getTransferDataAsLines(PDFTextStripperByArea stripper) {
        return on("\n").splitToList(stripper.getTextForRegion("class1"));
    }

    @Override
    @SneakyThrows
    public Collection<Transfer> get() {
        PDDocument document = null;
        try {
            document = PDDocument.load(file);
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition(true);
            stripper.addRegion("class1", rectangle);
            return getTransfers(document, stripper);
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }

    private Collection<Transfer> getTransfers(PDDocument document, PDFTextStripperByArea stripper) {
        PDPageTree pages = document.getPages();
        return createStreamFromIterator(pages.iterator())
                .map(pdPage -> getTransfers(pdPage, stripper))
                .flatMap(List::stream)
                .sorted(new TransferComparator())
                .collect(toList());
    }

    @SneakyThrows
    private List<Transfer> getTransfers(PDPage pdPage, PDFTextStripperByArea stripper) {
        stripper.extractRegions(pdPage);
        List<String> transferStrings = getTransferDataAsLines(stripper);
        return converter.convert(transferStrings).stream()
                .map(TransferRaw::asTransfer)
                .collect(toList());
    }

}