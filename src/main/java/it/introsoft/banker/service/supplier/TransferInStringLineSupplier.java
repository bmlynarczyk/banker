package it.introsoft.banker.service.supplier;

import com.google.common.collect.Lists;
import it.introsoft.banker.model.jpa.Transfer;
import it.introsoft.banker.model.raw.TransferRaw;
import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class TransferInStringLineSupplier implements Supplier<Collection<Transfer>> {

    private File file;
    private Converter<String, TransferRaw> converter;

    public TransferInStringLineSupplier(File file, Converter<String, TransferRaw> converter) {
        this.file = file;
        this.converter = converter;
    }

    @Override
    @SneakyThrows
    public Collection<Transfer> get() {
        List<Transfer> transfers = Lists.newArrayList();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                transfers.add(getTransfers(line));
            }
        }
        transfers.sort(new TransferComparator());
        return transfers;
    }

    private Transfer getTransfers(String transferLine) {
        return converter.convert(transferLine).asTransfer();
    }

}