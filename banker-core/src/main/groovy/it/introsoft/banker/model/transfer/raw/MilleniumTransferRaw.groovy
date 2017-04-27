package it.introsoft.banker.model.transfer.raw

import groovy.transform.EqualsAndHashCode
import it.introsoft.banker.model.Bank
import it.introsoft.banker.model.transfer.Transfer
import it.introsoft.banker.model.transfer.supplier.MoneyConverter
import it.introsoft.banker.model.transfer.type.TransferType
import it.introsoft.banker.model.transfer.type.TransferTypeRecognizer

@EqualsAndHashCode
class MilleniumTransferRaw implements TransferRaw {

    private static final TransferTypeRecognizer transferTypeRecognizer = Bank.MILLENIUM.typeRecognizer()

    String account
    String accountedAmount
    String accountingDate
    String transferDate
    String beneficiaryAccount
    String beneficiaryBank
    String beneficiaryName
    String cardName
    String cardNumber
    String cardOwner
    String date
    String dateTransferNumber
    String payeeAccount
    String payeeBank
    String payeeName
    String title
    String transferType

    @Override
    Transfer asTransfer() {
        def bank = Bank.MILLENIUM
        transferType = transferType.replaceAll('Typ operacji ', '')
        String amount = accountedAmount.replaceAll('Kwota zaksięgowana ', '')
        TransferType transferType = transferTypeRecognizer.recognize(transferType, amount)
        return new Transfer(
                account: account?.replaceAll('Z rachunku ', ''),
                amount: MoneyConverter.milleniumStringToMoneyValue(amount),
                bank: bank.name,
                beneficiaryAccount: beneficiaryAccount?.replaceAll('Na rachunek ', ''),
                beneficiaryName: beneficiaryName?.replaceAll('Odbiorca ', ''),
                beneficiaryBank: beneficiaryBank?.replaceAll('Bank odbiorcy ', ''),
                cardNumber: cardNumber?.replaceAll('Numer karty ', ''),
                cardName: cardName?.replaceAll('Karta ', ''),
                cardOwner: cardOwner?.replaceAll('Posiadacz karty ', ''),
                currency: 'PLN',
                date: new Date().parse('yyyy-MM-dd', date.replaceAll('Data księgowania ', '')),
                dateTransferNumber: Long.parseLong(dateTransferNumber.replaceAll('Dzienny numer transakcji ', '')),
                description: title?.replaceAll('Tytuł ', ''),
                payeeAccount: payeeAccount?.replaceAll('Z rachunku ', ''),
                payeeName: payeeName?.replaceAll('Zleceniodawca ', ''),
                transferType: transferType
        )
    }

}
