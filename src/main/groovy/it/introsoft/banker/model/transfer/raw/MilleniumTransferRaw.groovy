package it.introsoft.banker.model.transfer.raw

import it.introsoft.banker.model.Bank
import it.introsoft.banker.model.transfer.Transfer
import it.introsoft.banker.model.transfer.supplier.MoneyConverter
import it.introsoft.banker.model.transfer.type.TransferType
import it.introsoft.banker.model.transfer.type.TransferTypeRecognizer

class MilleniumTransferRaw implements TransferRaw {

    String account
    String title
    String transferType
    String date
    String accountedAmount
    String amount
    String beneficiaryAccount

    private static final TransferTypeRecognizer transferTypeRecognizer = Bank.MILLENIUM.typeRecognizer()

    @Override
    Transfer asTransfer() {
        def bank = Bank.MILLENIUM
        String amount
        if (accountedAmount)
            amount = accountedAmount.replaceAll('Kwota zaksięgowana ', '')
        else
            amount = this.amount.replaceAll('Kwota ', '')
        title = title.replaceAll('Tytuł ', '')
        transferType = transferType.replaceAll('Typ operacji ', '')
        TransferType transferType = transferTypeRecognizer.recognize(transferType, amount)
        return new Transfer(
                account: account,
                beneficiaryAccount: beneficiaryAccount?.replaceAll('Na rachunek ', ''),
                date: new Date().parse('yyyy-MM-dd', date.replaceAll('Data księgowania ', '')),
                amount: MoneyConverter.milleniumStringToMoneyValue(amount),
                description: title,
                transferType: transferType,
                currency: 'PLN',
                bank: bank.name
        )
    }

}
