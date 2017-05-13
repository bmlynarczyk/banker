package it.introsoft.banker.model.transfer.supplier;

import it.introsoft.banker.model.transfer.Transfer;
import it.introsoft.banker.model.transfer.TransferComparator;
import it.introsoft.banker.model.transfer.raw.TransferRaw;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.core.convert.converter.Converter;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TransferInHtmlTableSupplier implements Supplier<Collection<Transfer>> {

    private File file;
    private Converter<Document, List<TransferRaw>> converter;

    public TransferInHtmlTableSupplier(File file, Converter<Document, List<TransferRaw>> converter) {
        this.file = file;
        this.converter = converter;
    }

    @Override
    @SneakyThrows
    public Collection<Transfer> get() {
        Document document = Jsoup.parse(file, "utf-8");
        return converter.convert(document).stream()
                .map(TransferRaw::asTransfer)
                .sorted(new TransferComparator())
                .collect(Collectors.toList());
    }

}
