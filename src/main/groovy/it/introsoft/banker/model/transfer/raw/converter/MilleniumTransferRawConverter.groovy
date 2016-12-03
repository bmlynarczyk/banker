package it.introsoft.banker.model.transfer.raw.converter

import groovy.transform.CompileStatic
import it.introsoft.banker.model.transfer.raw.MilleniumTransferRaw
import org.springframework.core.convert.converter.Converter

@CompileStatic
class MilleniumTransferRawConverter implements Converter<List<String>, MilleniumTransferRaw> {

    private String account

    MilleniumTransferRawConverter(String account) {
        this.account = account
    }

    @Override
    MilleniumTransferRaw convert(List<String> strings) {
        return new MilleniumTransferRaw(
                account: account,
                beneficiaryAccount: strings.find { it.startsWith('Na rachunek ') },
                title: strings.find { it.startsWith('Tytuł ') },
                type: strings.find { it.startsWith('Typ operacji ') },
                date: strings.find { it.startsWith('Data księgowania ') },
                amount: strings.find { it.startsWith('Kwota zaksięgowana ') }
        )
    }

}
