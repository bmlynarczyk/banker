package it.introsoft.banker.model.raw;

import it.introsoft.banker.model.jpa.Transfer;
import it.introsoft.banker.service.type.BgzOptimaTransferTypeRecognizer;
import it.introsoft.banker.service.type.TransferTypeRecognizer;
import lombok.*;

import java.text.SimpleDateFormat;

import static it.introsoft.banker.model.raw.MoneyConverter.bgzOptimaStringToMoneyValue;

@AllArgsConstructor
@Builder
@Value
@EqualsAndHashCode
public class BgzOptimaTransferRaw implements TransferRaw {

    private static final TransferTypeRecognizer transferTypeRecognizer = new BgzOptimaTransferTypeRecognizer();

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
                .transferType(transferTypeRecognizer.recognize(transferType, amount))
                .date(new SimpleDateFormat("dd.MM.yyyy").parse(date))
                .amount(bgzOptimaStringToMoneyValue(amount))
                .balance(bgzOptimaStringToMoneyValue(balance))
                .bank(Bank.BGZ_OPTIMA)
                .description(title)
                .build();
    }

}
