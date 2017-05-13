package it.introsoft.banker.model.transfer.raw.converter;

import it.introsoft.banker.model.transfer.raw.BgzOptimaTransferRaw;
import it.introsoft.banker.model.transfer.raw.TransferRaw;
import org.springframework.core.convert.converter.Converter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

public class BgzOptimaTransferRawConverter implements Converter<List<String>, List<TransferRaw>> {

    private static final Pattern transferPattern = Pattern.compile("(\\d*) (\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d) ([^\\d.]*) (.* PLN) (.* PLN)");
    private final String account;

    public BgzOptimaTransferRawConverter(String account) {
        this.account = account;
    }

    @Override
    public List<TransferRaw> convert(List<String> strings) {
        return strings.stream()
                .filter(it -> transferPattern.matcher(it).matches())
                .map(transferPattern::matcher)
                .peek(Matcher::matches)
                .map(this::createTransferRaw)
                .collect(toList());
    }

    private BgzOptimaTransferRaw createTransferRaw(Matcher matcher) {
        String transferType = matcher.group(3);
        return BgzOptimaTransferRaw.builder()
                .account(account)
                .date(matcher.group(2))
                .transferType(transferType)
                .amount(matcher.group(4))
                .balance(matcher.group(5))
                .title(transferType)
                .build();
    }

}
