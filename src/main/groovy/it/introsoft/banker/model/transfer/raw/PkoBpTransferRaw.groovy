package it.introsoft.banker.model.transfer.raw

import it.introsoft.banker.model.Bank
import it.introsoft.banker.model.transfer.Transfer

import static it.introsoft.banker.model.transfer.supplier.MoneyConverter.toMoneyValue

class PkoBpTransferRaw implements TransferRaw{

    String account
    String description
    String type
    String date
    String amount
    String currency
    String balance

    @Override
    Transfer asTransfer() {
        return new Transfer(
                account: account,
                bank: Bank.PKO_BP.name,
                currency: currency,
                type: Bank.PKO_BP.typeRecognizer().recognize(type, amount),
                date: new Date().parse('yyyy-MM-dd', date),
                amount: toMoneyValue(getMoneyString(amount)),
                balance: toMoneyValue(getMoneyString(balance)),
                description: description
        )
    }

    private static String getMoneyString(String value) {
        return value.replaceAll('\\+', '').replaceAll('\\.',',')
    }


}
