package it.introsoft.banker.model.view;

import it.introsoft.banker.model.raw.TransferType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
@AllArgsConstructor
public class AccountReportTransfer {

    private TransferType transferType;
    private Long amount;
    private Long balance;
    private LocalDate date;
    private Long dateTransferNumber;

}
