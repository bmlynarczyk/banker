package it.introsoft.banker.service.converter;

import it.introsoft.banker.model.raw.TransferRaw;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

abstract class BaseTransferRawConverter implements Converter<List<String>, TransferRaw> {

    String findLineWithPrefix(String prefix, List<String> lines) {
        return lines.stream().filter(string -> string.startsWith(prefix)).findFirst().orElse(null);
    }

    String findLineWithPrefix(String prefix1, String prefix2, List<String> lines) {
        return lines.stream().filter(string -> string.startsWith(prefix1) || string.startsWith(prefix2)).findFirst().orElse(null);
    }

}
