package it.introsoft.banker.service;

import com.google.common.eventbus.Subscribe;
import it.introsoft.banker.repository.Account;
import it.introsoft.banker.repository.AccountRepository;
import it.introsoft.banker.repository.H2Transfer;
import it.introsoft.banker.repository.TransferRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Slf4j
@Service
public class UpdateAccountEventConsumer implements Consumer<UpdateAccountEvent> {

    private final AccountRepository accountRepository;

    private final TransferRepository transferRepository;

    @Autowired
    public UpdateAccountEventConsumer(AccountRepository accountRepository, TransferRepository transferRepository) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }

    @Subscribe
    @Override
    public void accept(UpdateAccountEvent updateAccountEvent) {
        Account account = accountRepository.findByNumber(updateAccountEvent.getAccount())
                .orElseThrow(IllegalArgumentException::new);
        H2Transfer transfer = transferRepository.findFirstByAccountOrderByDateDescDateTransferNumberDesc(account.getNumber())
                .orElseThrow(IllegalArgumentException::new);

        account.setCurrentBalance(transfer.getBalance());
        accountRepository.save(account);

        log.info("account {} updated", account.getNumber());
    }
}