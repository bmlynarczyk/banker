package it.introsoft.banker.model.transfer.raw.converter

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import it.introsoft.banker.model.transfer.raw.MilleniumTransferRaw
import org.springframework.core.convert.converter.Converter

@Slf4j
@CompileStatic
class MilleniumTransferRawConverter implements Converter<List<String>, MilleniumTransferRaw> {

    private String account

    MilleniumTransferRawConverter(String account) {
        this.account = account
    }

    @Override
    MilleniumTransferRaw convert(List<String> strings) {
        log.debug(strings.toString())
        return new MilleniumTransferRaw(
                transferType: strings.find { it.startsWith('Typ operacji ') },
                dateTransferNumber: strings.find { it.startsWith('Dzienny numer transakcji ') },
                date: strings.find { it.startsWith('Data księgowania ') },
                accountingDate: strings.find { it.startsWith('Data waluty ') },
                transferDate: strings.find { it.startsWith('Data transakcji ') },
                payeeAccount: strings.find { it.startsWith('Z rachunku ') },
                payeeBank: strings.find { it.startsWith('Bank zleceniodawcy ') },
                payeeName: strings.find { it.startsWith('Zleceniodawca ') },
                cardName: strings.find { it.startsWith('Karta ') },
                cardNumber: strings.find { it.startsWith('Numer karty ') },
                cardOwner: strings.find { it.startsWith('Posiadacz karty ') },
                beneficiaryAccount: strings.find { it.startsWith('Na rachunek ') },
                beneficiaryBank: strings.find { it.startsWith('Bank odbiorcy ') },
                beneficiaryName: strings.find { it.startsWith('Odbiorca ') },
                title: strings.find { it.startsWith('Tytuł ') },
                accountedAmount: strings.find { it.startsWith('Kwota zaksięgowana ') },
                account: account

        )
    }

}
