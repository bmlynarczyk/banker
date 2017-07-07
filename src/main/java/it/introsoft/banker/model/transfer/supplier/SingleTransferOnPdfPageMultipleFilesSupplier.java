package it.introsoft.banker.model.transfer.supplier;

import com.google.common.collect.Lists;
import it.introsoft.banker.model.transfer.TransferComparator;
import it.introsoft.banker.model.transfer.raw.TransferRaw;
import it.introsoft.banker.repository.Transfer;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.core.convert.converter.Converter;

import java.awt.*;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.google.common.base.Splitter.on;
import static org.springframework.data.util.StreamUtils.createStreamFromIterator;

public class SingleTransferOnPdfPageMultipleFilesSupplier implements Supplier<Collection<Transfer>> {

    private File file;
    private Rectangle rectangle;
    private Converter<List<String>, TransferRaw> converter;

    public SingleTransferOnPdfPageMultipleFilesSupplier(File file, Rectangle rectangle, Converter<List<String>, TransferRaw> converter) {
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
        Collection<Transfer> transfers = Lists.newArrayList();

        for (File childFile : file.listFiles()) {
            try {
                document = PDDocument.load(childFile);
                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);
                stripper.addRegion("class1", rectangle);
                transfers.addAll(getTransfers(document, stripper));
            } finally {
                if (document != null) {
                    document.close();
                }
            }
        }

        return transfers;
    }

    private Collection<Transfer> getTransfers(PDDocument document, PDFTextStripperByArea stripper) {
        return createStreamFromIterator(document.getPages().iterator())
                .map((PDPage page) -> getTransfer(page, stripper))
                .sorted(new TransferComparator())
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private Transfer getTransfer(PDPage page, PDFTextStripperByArea stripper) {
        stripper.extractRegions(page);
        return converter.convert(getTransferDataAsLines(stripper)).asTransfer();
    }

}