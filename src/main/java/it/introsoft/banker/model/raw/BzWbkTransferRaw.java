package it.introsoft.banker.model.raw;

import it.introsoft.banker.model.jpa.Transfer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;

import static it.introsoft.banker.model.raw.Bank.BZ_WBK;
import static it.introsoft.banker.model.raw.TransferRawUtils.bzWbkStringToMoneyValue;
import static it.introsoft.banker.model.raw.TransferRawUtils.toLocalDate;


@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class BzWbkTransferRaw implements TransferRaw {

    private String account;
    private String amount;
    private String balance;
    private String accountingDate;
    private String operationDate;
    private String beneficiaryAccount;
    private String beneficiaryName;
    private String title;
    private TransferType transferType;

    @Override
    @SneakyThrows
    public Transfer asTransfer() {
        return Transfer.builder()
                .account(account)
                .amount(bzWbkStringToMoneyValue(amount))
                .balance(bzWbkStringToMoneyValue(balance))
                .bank(BZ_WBK)
                .beneficiaryAccount(beneficiaryAccount)
                .beneficiaryName(beneficiaryName)
                .currency("PLN")
                .date(toLocalDate(operationDate, "dd-MM-yyyy"))
                .description(title)
                .transferType(transferType)
                .build();
    }

}
