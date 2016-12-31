package it.introsoft.banker.model.transfer.raw

import groovy.transform.EqualsAndHashCode
import it.introsoft.banker.model.Bank
import it.introsoft.banker.model.transfer.Transfer
import it.introsoft.banker.model.transfer.type.TransferTypeRecognizer

import static it.introsoft.banker.model.transfer.supplier.MoneyConverter.bgzOptimaStringToMoneyValue

@EqualsAndHashCode
class BgzOptimaTransferRaw implements TransferRaw {

    String account
    String title
    String transferType
    String date
    String amount
    String balance
    String beneficiaryAccount

    private static final TransferTypeRecognizer transferTypeRecognizer = Bank.BGZ_OPTIMA.typeRecognizer()

    @Override
    Transfer asTransfer() {

        return new Transfer(
                account: account,
                transferType: transferTypeRecognizer.recognize(transferType, amount),
                date: new Date().parse('dd.MM.yyyy', date),
                amount: bgzOptimaStringToMoneyValue(amount),
                balance: bgzOptimaStringToMoneyValue(balance),
                bank: Bank.BGZ_OPTIMA.name,
                description: title
        )
    }
}
