package it.introsoft.banker.model.transfer;

import it.introsoft.banker.repository.Transfer;

import java.util.Comparator;

public class TransferComparator implements Comparator<Transfer> {

    @Override
    public int compare(Transfer transfer1, Transfer transfer2) {
        int dateComparison = transfer1.getDate().compareTo(transfer2.getDate());
        if (dateComparison != 0)
            return dateComparison;
        if (transfer1.getDateTransferNumber() != null)
            return transfer1.getDateTransferNumber().compareTo(transfer2.getDateTransferNumber());
        if (transfer1.getBalance() != null)
            return -1;
        throw new IllegalStateException("sort is impossible");
    }

}
