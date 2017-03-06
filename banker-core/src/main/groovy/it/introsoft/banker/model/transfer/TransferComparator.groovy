package it.introsoft.banker.model.transfer

class TransferComparator implements Comparator<Transfer> {

    @Override
    int compare(Transfer transfer1, Transfer transfer2) {
        def dateComparison = transfer1.date <=> transfer2.date
        if (dateComparison != 0)
            return dateComparison
        if (transfer1.dateTransferNumber)
            return transfer1.dateTransferNumber <=> transfer2.dateTransferNumber
        if (transfer1.balance)
            return -1
        throw new IllegalStateException('sort is impossible')
    }

}
