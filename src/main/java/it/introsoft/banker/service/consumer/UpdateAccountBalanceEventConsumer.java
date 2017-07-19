package it.introsoft.banker.service.consumer;

import com.google.common.eventbus.Subscribe;
import it.introsoft.banker.model.jpa.Account;
import it.introsoft.banker.model.jpa.Transfer;
import it.introsoft.banker.repository.AccountRepository;
import it.introsoft.banker.repository.TransferRepository;
import it.introsoft.banker.service.event.UpdateAccountBalanceEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Slf4j
@Service
public class UpdateAccountBalanceEventConsumer implements Consumer<UpdateAccountBalanceEvent> {

    private final AccountRepository accountRepository;

    private final TransferRepository transferRepository;

    @Autowired
    public UpdateAccountBalanceEventConsumer(AccountRepository accountRepository, TransferRepository transferRepository) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }

    @Subscribe
    @Override
    public void accept(UpdateAccountBalanceEvent updateAccountBalanceEvent) {
        Account account = updateAccountBalanceEvent.getAccount();
        Transfer transfer = transferRepository.findFirstByAccountOrderByDateDescDateTransferNumberDesc(account.getNumber())
                .orElseThrow(IllegalArgumentException::new);

        Long balance = transfer.getBalance();

        if (balance != null) {
            account.setCurrentBalance(balance);
            accountRepository.save(account);
            log.info("balance of account {} updated", account.getNumber());
        }

    }

}
