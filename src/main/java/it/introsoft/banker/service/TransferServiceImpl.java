package it.introsoft.banker.service;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Iterators;
import com.querydsl.core.types.dsl.BooleanExpression;
import it.introsoft.banker.model.transfer.Transfer;
import it.introsoft.banker.repository.H2Transfer;
import it.introsoft.banker.repository.TransferRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Iterator;
import java.util.Optional;

import static it.introsoft.banker.repository.QH2Transfer.h2Transfer;

@Slf4j
@Component
public class TransferServiceImpl implements TransferService {
    private final TransferRepository transferRepository;
    private final BalanceCalculator balanceCalculator;

    @Autowired
    public TransferServiceImpl(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
        this.balanceCalculator = new BalanceCalculator(transferRepository);
    }

    @Override
    public Result save(Transfer transfer) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Optional<H2Transfer> fromDb = findTransfer(transfer);
        if (fromDb.isPresent()) {
            log.info("transfer already exists with id: {}", fromDb.get().getId());
            return Result.EXISTING;
        } else {
            saveInRepo(transfer);
            log.debug("transfer saved in: {}", stopwatch);
            return Result.SAVED;
        }

    }

    private Optional<H2Transfer> findTransfer(Transfer transfer) {
        Stopwatch stopwatch = Stopwatch.createStarted();

        BooleanExpression predicate = (h2Transfer.date.eq(transfer.getDate()).and(h2Transfer.account.eq(transfer.getAccount())).and(h2Transfer.amount.eq(transfer.getAmount())).and(h2Transfer.description.eq(transfer.getDescription())));

        if (transfer.getBalance() != null)
            predicate = predicate.and(h2Transfer.balance.eq(transfer.getBalance()));

        if (transfer.getDateTransferNumber() != null)
            predicate = predicate.and(h2Transfer.dateTransferNumber.eq(transfer.getDateTransferNumber()));

        log.trace(predicate.toString());
        H2Transfer existingTransfer = transferRepository.findOne(predicate);
        log.debug("check existence of transfer in: {}", stopwatch);
        return Optional.ofNullable(existingTransfer);
    }

    private void saveInRepo(Transfer transfer) {
        final Long number = transfer.getDateTransferNumber();
        transferRepository.save(H2Transfer.builder().date(transfer.getDate()).dateTransferNumber(number != null ? number : getDateTransferNumber(transfer.getDate())).description(transfer.getDescription()).amount(transfer.getAmount()).balance(balanceCalculator.calculate(transfer)).currency(transfer.getCurrency()).transferType(transfer.getTransferType()).account(transfer.getAccount()).beneficiaryName(transfer.getBeneficiaryName()).beneficiaryAccount(transfer.getBeneficiaryAccount()).beneficiaryAddress(transfer.getBeneficiaryAddress()).payeeName(transfer.getPayeeName()).payeeAccount(transfer.getPayeeAccount()).payeeAddress(transfer.getPayeeAddress()).bank(transfer.getBank()).category(transfer.getCategory()).cardNumber(transfer.getCardNumber()).build());
    }

    private Long getDateTransferNumber(Date date) {
        Iterator<H2Transfer> iterator = transferRepository
                .findAll(h2Transfer.date.eq(date), h2Transfer.dateTransferNumber.desc())
                .iterator();

        H2Transfer transfer = Iterators.getNext(iterator, null);
        return (transfer != null ? transfer.getDateTransferNumber() : 0L) + 1;
    }

    @Override
    public void deleteAll() {
        transferRepository.deleteAll();
    }

}
