package it.introsoft.banker.service;

import com.google.common.eventbus.EventBus;
import it.introsoft.banker.model.jpa.Account;
import it.introsoft.banker.model.jpa.QTransfer;
import it.introsoft.banker.model.jpa.Transfer;
import it.introsoft.banker.repository.AccountRepository;
import it.introsoft.banker.repository.TransferRepository;
import it.introsoft.banker.service.event.UpdateAccountBalanceEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BalanceCalculator {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;
    private final EventBus eventBus;

    private final QTransfer qtransfer = QTransfer.transfer;

    private final Sort transfersSort = new Sort(
            new Sort.Order(Sort.Direction.ASC, "date"),
            new Sort.Order(Sort.Direction.ASC, "dateTransferNumber")
    );

    @Autowired
    public BalanceCalculator(AccountRepository accountRepository, TransferRepository transferRepository, EventBus eventBus) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
        this.eventBus = eventBus;
    }

    public void recalculateBalanceForAllTransfers(String accountNumber) {
        Account account = accountRepository.getOne(accountNumber);

        Long balance = 0L;
        int pageNumber = 0;
        Page<Transfer> transfersPage;

        do {
            transfersPage = getPage(account, pageNumber);

            for (Transfer transfer : transfersPage.getContent()) {
                balance = balance + transfer.getAmount();
                transfer.setBalance(balance);
                transferRepository.save(transfer);
            }

            pageNumber = transfersPage.getNumber() + 1;
        } while (transfersPage.hasNext());

        eventBus.post(UpdateAccountBalanceEvent.builder().account(account).build());
    }

    private Page<Transfer> getPage(Account account, int pageNumber) {
        return transferRepository.findAll(qtransfer.account.eq(account.getNumber()),
                new PageRequest(pageNumber, 100, transfersSort)
        );
    }

}
