package it.introsoft.banker.service;

import com.google.common.base.Stopwatch;
import com.querydsl.core.types.dsl.BooleanExpression;
import it.introsoft.banker.repository.QTransfer;
import it.introsoft.banker.repository.Transfer;
import it.introsoft.banker.repository.TransferRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Iterator;
import java.util.Optional;

@Slf4j
@Component
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;
    private final BeneficiaryCollector beneficiaryCollector;
    private final PayeeCollector payeeCollector;
    private final DescriptorCollector descriptorCollector;

    private final QTransfer qtransfer = QTransfer.transfer;

    @Autowired
    public TransferServiceImpl(TransferRepository transferRepository,
                               BeneficiaryCollector beneficiaryCollector,
                               PayeeCollector payeeCollector, DescriptorCollector descriptorCollector) {
        this.transferRepository = transferRepository;
        this.beneficiaryCollector = beneficiaryCollector;
        this.payeeCollector = payeeCollector;
        this.descriptorCollector = descriptorCollector;
    }

    @Override
    public Result save(Transfer transfer) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        beneficiaryCollector.accept(transfer);
        payeeCollector.accept(transfer);
        descriptorCollector.accept(transfer);
        Optional<Transfer> fromDb = findTransfer(transfer);
        if (fromDb.isPresent()) {
            log.info("transfer already exists with id: {}", fromDb.get().getId());
            return Result.EXISTING;
        } else {
            saveInRepo(transfer);
            log.debug("transfer saved in: {}", stopwatch);
            return Result.SAVED;
        }

    }

    private Optional<Transfer> findTransfer(Transfer transfer) {
        Stopwatch stopwatch = Stopwatch.createStarted();

        BooleanExpression predicate = (qtransfer.date.eq(transfer.getDate())
                .and(qtransfer.account.eq(transfer.getAccount()))
                .and(qtransfer.amount.eq(transfer.getAmount()))
                .and(qtransfer.description.eq(transfer.getDescription())));

        if (transfer.getBalance() != null)
            predicate = predicate.and(qtransfer.balance.eq(transfer.getBalance()));

        if (transfer.getDateTransferNumber() != null)
            predicate = predicate.and(qtransfer.dateTransferNumber.eq(transfer.getDateTransferNumber()));

        log.trace(predicate.toString());
        Transfer existingTransfer = transferRepository.findOne(predicate);
        log.debug("check existence of transfer in: {}", stopwatch);
        return Optional.ofNullable(existingTransfer);
    }

    private void saveInRepo(Transfer transfer) {
        final Long number = transfer.getDateTransferNumber();
        transfer.setDateTransferNumber(number != null ? number : getDateTransferNumber(transfer.getDate()));
        transferRepository.save(transfer);
    }

    private Long getDateTransferNumber(Date date) {
        Iterator<Transfer> iterator = transferRepository
                .findAll(qtransfer.date.eq(date), qtransfer.dateTransferNumber.desc())
                .iterator();

        return (iterator.hasNext() ? iterator.next().getDateTransferNumber() : 0L) + 1;
    }

}
