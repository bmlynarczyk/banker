package it.introsoft.banker.view;

import it.introsoft.banker.model.raw.TransferType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Value
@Builder
@AllArgsConstructor
public class AccountReportTransfer {

    private TransferType transferType;
    private Long amount;
    private Long balance;
    private Date date;
    private Long dateTransferNumber;

}
