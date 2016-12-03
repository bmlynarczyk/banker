package it.introsoft.banker.model.transfer.raw

import it.introsoft.banker.model.Bank
import it.introsoft.banker.model.transfer.Transfer
import it.introsoft.banker.model.transfer.supplier.MoneyConverter
import it.introsoft.banker.model.transfer.type.TransferType

class MilleniumTransferRaw implements TransferRaw {

    String account
    String title
    String type
    String date
    String amount
    String beneficiaryAccount

    @Override
    Transfer asTransfer() {
        def bank = Bank.MILLENIUM
        amount = amount.replaceAll('Kwota zaksięgowana ', '')
        title = title.replaceAll('Tytuł ', '')
        type = type.replaceAll('Typ operacji ', '')
        TransferType transferType = bank.typeRecognizer().recognize(type, amount)
        return new Transfer(
                account: account,
                beneficiaryAccount: beneficiaryAccount?.replaceAll('Na rachunek ', ''),
                date: new Date().parse('yyyy-MM-dd', date.replaceAll('Data księgowania ', '')),
                amount: MoneyConverter.milleniumStringToMoneyValue(amount),
                description: title,
                type: transferType,
                currency: 'PLN',
                bank: bank.name,
                category: bank.categoryRecognizer().recognize(title, amount)
        )
    }

}
