package it.introsoft.banker.model.transfer.raw.converter

import it.introsoft.banker.model.transfer.raw.PkoBpTransferRaw
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.springframework.core.convert.converter.Converter

class PkoBpTransferRawConverter implements Converter<Document, List<PkoBpTransferRaw>> {

    private final String account

    PkoBpTransferRawConverter(String account) {
        this.account = account
    }

    @Override
    List<PkoBpTransferRaw> convert(Document source) {
        Elements rows = source.select('table').last().select('tr:gt(0)')
        return rows.collect {
            def columns = it.select('td')
            return new PkoBpTransferRaw(
                account: account,
                date: columns.get(0).text(),
                transferType: columns.get(2).text(),
                description: columns.get(3).text(),
                amount: columns.get(4).text(),
                currency: columns.get(5).text(),
                balance: columns.get(6).text()
            )
        }
    }

}
