package it.introsoft.banker.model.transfer.raw.converter;

import it.introsoft.banker.model.transfer.raw.TransferRaw;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

public abstract class BaseTransferRawConverter implements Converter<List<String>, TransferRaw> {

    protected String findLineWithPrefix(String prefix, List<String> lines) {
        return lines.stream().filter(string -> string.startsWith(prefix)).findFirst().orElse(null);
    }

    protected String findLineWithPrefix(String prefix1, String prefix2, List<String> lines) {
        return lines.stream().filter(string -> string.startsWith(prefix1) || string.startsWith(prefix2)).findFirst().orElse(null);
    }

}
