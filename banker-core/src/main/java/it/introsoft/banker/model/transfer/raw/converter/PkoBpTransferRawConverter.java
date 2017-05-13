package it.introsoft.banker.model.transfer.raw.converter;

import it.introsoft.banker.model.transfer.raw.PkoBpTransferRaw;
import it.introsoft.banker.model.transfer.raw.TransferRaw;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class PkoBpTransferRawConverter implements Converter<Document, List<TransferRaw>> {

    private final String account;

    public PkoBpTransferRawConverter(String account) {
        this.account = account;
    }

    @Override
    public List<TransferRaw> convert(Document source) {
        Elements rows = source.select("table").last().select("tr:gt(0)");
        return rows.stream()
                .map(element -> element.select("td"))
                .map(elements -> PkoBpTransferRaw.builder()
                        .account(account)
                        .date(elements.get(0).text())
                        .transferType(elements.get(2).text())
                        .description(elements.get(3).text())
                        .amount(elements.get(4).text())
                        .currency(elements.get(5).text())
                        .balance(elements.get(6).text())
                        .build())
                .collect(toList());
    }
}
