package it.introsoft.banker.model.transfer.raw

import groovy.transform.EqualsAndHashCode
import groovy.util.logging.Slf4j
import it.introsoft.banker.model.Bank
import it.introsoft.banker.model.transfer.Transfer
import it.introsoft.banker.model.transfer.supplier.MoneyConverter
import it.introsoft.banker.model.transfer.type.TransferType
import it.introsoft.banker.model.transfer.type.TransferTypeRecognizer

@Slf4j
@EqualsAndHashCode
class MBankTransferRaw implements TransferRaw {

    String account
    String accounts
    String title
    String sender
    String transferType
    String date
    String referenceNumber
    String amount
    String symbol

    private static final TransferTypeRecognizer transferTypeRecognizer = Bank.M_BANK.typeRecognizer()

    @Override
    Transfer asTransfer() {
        boolean isZus = sender != null && sender.contains('ZUS')
        boolean isTax = symbol != null
        def description = getDescription(isZus, isTax)
        def bank = Bank.M_BANK
        amount = amount.replaceAll('Kwota przelewu: ', '')
        amount = amount.replaceAll('Kwota w PLN: ', '')
        TransferType transferType = transferTypeRecognizer.recognize(getDescriber(isZus, isTax), amount)
        if (transferType in [TransferType.TAX_CHARGES, TransferType.INSURANCE_CHARGES, TransferType.BANK_CHARGES, TransferType.INTEREST_TAX_CHARGES, TransferType.CHARGES])
            amount = "-$amount"
        return new Transfer(
                account: account,
                beneficiaryAccount: accounts.substring(accounts.lastIndexOf('Nr Rachunku: ')).replaceAll('Nr Rachunku: ', ''),
                date: new Date().parse('yyyy-MM-dd', date.replaceAll('Data księgowania: ', '')),
                amount: MoneyConverter.mBankStringToMoneyValue(amount),
                description: description,
                transferType: transferType,
                currency: 'PLN',
                bank: bank.name,
                dateTransferNumber: getDateTransferNumber()
        )
    }

    long getDateTransferNumber() {
        referenceNumber = referenceNumber.replaceAll('Nr referencyjny operacji: ', '')
        referenceNumber = referenceNumber.replaceAll('Nr referencyjny: ', '')
        if (referenceNumber.contains('-'))
            referenceNumber = referenceNumber.split('-')[1]
        log.info(referenceNumber)
        referenceNumber = referenceNumber.replaceFirst('^0+(?!$)', '')
        log.info(referenceNumber)
        return Long.parseLong(referenceNumber)
    }

    private String getDescription(boolean isZus, boolean isTax) {
        if (isZus)
            return sender.substring(sender.indexOf('ZUS'))
        if (isTax)
            return symbol.replaceAll('Symbol: ', '')
        return title.replaceAll('Tytuł operacji: ', '')
    }

    private String getDescriber(boolean isZus, boolean isTax) {
        if (isZus)
            return 'zus'
        if (isTax)
            return 'us'
        return transferType.replaceAll('Rodzaj operacji: ', '')
    }
}