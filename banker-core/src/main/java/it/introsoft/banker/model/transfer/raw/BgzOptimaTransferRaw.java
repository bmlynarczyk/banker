package it.introsoft.banker.model.transfer.raw;

import it.introsoft.banker.model.Bank;
import it.introsoft.banker.model.transfer.Transfer;
import it.introsoft.banker.model.transfer.type.TransferTypeRecognizer;
import lombok.*;

import java.text.SimpleDateFormat;

import static it.introsoft.banker.model.transfer.supplier.MoneyConverter.bgzOptimaStringToMoneyValue;

@AllArgsConstructor
@Builder
@Value
@EqualsAndHashCode
public class BgzOptimaTransferRaw implements TransferRaw {

    private static final TransferTypeRecognizer transferTypeRecognizer = Bank.BGZ_OPTIMA.typeRecognizer();

    private String account;
    private String title;
    private String transferType;
    private String date;
    private String amount;
    private String balance;
    private String beneficiaryAccount;

    @Override
    @SneakyThrows
    public Transfer asTransfer() {
        return Transfer.builder()
                .account(account)
                .transferType(transferTypeRecognizer.recognize(transferType, amount).name())
                .date(new SimpleDateFormat("dd.MM.yyyy").parse(date))
                .amount(bgzOptimaStringToMoneyValue(amount))
                .balance(bgzOptimaStringToMoneyValue(balance))
                .bank(Bank.BGZ_OPTIMA.getName())
                .description(title)
                .build();
    }

}
