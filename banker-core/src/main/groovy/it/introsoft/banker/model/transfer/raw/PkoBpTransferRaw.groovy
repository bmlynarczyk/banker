package it.introsoft.banker.model.transfer.raw

import groovy.transform.EqualsAndHashCode
import it.introsoft.banker.model.Bank
import it.introsoft.banker.model.transfer.Transfer
import it.introsoft.banker.model.transfer.type.TransferTypeRecognizer

import static it.introsoft.banker.model.transfer.supplier.MoneyConverter.toMoneyValue

@EqualsAndHashCode
class PkoBpTransferRaw implements TransferRaw {

    String account
    String description
    String transferType
    String date
    String amount
    String currency
    String balance

    private static final TransferTypeRecognizer transferTypeRecognizer = Bank.PKO_BP.typeRecognizer()

    @Override
    Transfer asTransfer() {
        return new Transfer(
                account: account,
                bank: Bank.PKO_BP.name,
                currency: currency,
                transferType: transferTypeRecognizer.recognize(transferType, amount),
                date: new Date().parse('yyyy-MM-dd', date),
                amount: toMoneyValue(getMoneyString(amount)),
                balance: toMoneyValue(getMoneyString(balance)),
                description: description
        )
    }

    private static String getMoneyString(String value) {
        return value.replaceAll('\\+', '').replaceAll('\\.', ',')
    }


}
