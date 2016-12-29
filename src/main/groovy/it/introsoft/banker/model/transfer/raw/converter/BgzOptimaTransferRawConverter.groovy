package it.introsoft.banker.model.transfer.raw.converter

import groovy.util.logging.Slf4j
import it.introsoft.banker.model.transfer.raw.BgzOptimaTransferRaw
import it.introsoft.banker.model.transfer.raw.TransferRaw
import org.springframework.core.convert.converter.Converter

import java.util.regex.Pattern

@Slf4j
class BgzOptimaTransferRawConverter implements Converter<List<String>, List<TransferRaw>> {

    private final String account

    private static
    final Pattern bgzTransferPattern = Pattern.compile('(\\d*) (\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d) ([^\\d.]*) (.* PLN) (.* PLN)')

    BgzOptimaTransferRawConverter(String account) {
        this.account = account
    }

    @Override
    List<TransferRaw> convert(List<String> strings) {
        def result = []
        int position = 0
        strings.each {
            def matcher = bgzTransferPattern.matcher(it)
            if (matcher.matches()) {
                def transferType = matcher.group(3)
                result.add(new BgzOptimaTransferRaw(
                        account: account,
                        date: matcher.group(2),
                        transferType: transferType,
                        amount: matcher.group(4),
                        balance: matcher.group(5),
                        title: getTitle(strings, transferType, position)
                ))
            }
            position++
        }
        return result
    }

    private static String getTitle(List<String> transfers, String currentTransferType, int currentTransferPosition) {
        if (currentTransferType in ['ODSETKI ZAPŁACONE', 'PODATEK POBRANY', 'ODSETKI OTRZYMANE'])
            return null
        if (currentTransferType in ['PRZELEW'])
            return transfers.get(currentTransferPosition + 3)
        if (currentTransferType in ['PRZELEW PLANET'])
            return transfers.get(currentTransferPosition + 3)
        if (currentTransferType in ['PRZELEW OTRZYMANY ELIXIR', 'PRZELEW OTRZYMANY'])
            return transfers.get(currentTransferPosition + 5)
        throw new IllegalStateException(currentTransferType)
    }

}
