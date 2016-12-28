package it.introsoft.banker.model.transfer.raw.converter

import it.introsoft.banker.model.transfer.raw.MBankTransferRaw
import org.springframework.core.convert.converter.Converter

class MBankTransferRawConverter implements Converter<List<String>, MBankTransferRaw> {

    private final String account

    MBankTransferRawConverter(String account) {
        this.account = account
    }

    @Override
    MBankTransferRaw convert(List<String> strings) {
        return new MBankTransferRaw(
                account: account,
                accounts: strings.find { it.startsWith('Nr Rachunku: ') },
                title: strings.find { it.startsWith('Tytuł operacji: ') },
                sender: strings.find { it.startsWith('Nadawca: ') },
                symbol: strings.find { it.startsWith('Symbol: ') },
                transferType: strings.find { it.startsWith('Rodzaj operacji: ') },
                date: strings.find { it.startsWith('Data księgowania: ') },
                amount: getAmount(strings)
        )
    }

    private static String getAmount(List<String> transferStrings) {
        def foundedAmount = transferStrings.find { it.startsWith('Kwota przelewu: ') }
        if (!foundedAmount)
            return transferStrings.find { it.startsWith('Kwota w PLN: ') }
        return transferStrings.find { it.startsWith('Kwota przelewu: ') }
    }


}
