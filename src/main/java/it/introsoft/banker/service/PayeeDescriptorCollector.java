package it.introsoft.banker.service;

import it.introsoft.banker.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static it.introsoft.banker.repository.QPayeeDescriptor.payeeDescriptor;

@Component
public class PayeeDescriptorCollector implements Consumer<Transfer> {
    private final PayeeDescriptorRepository repository;

    private Predicate<Transfer> containsName = transfer -> null != transfer.getPayeeName();
    private Predicate<Transfer> containsAccount = transfer -> null != transfer.getPayeeAccount();
    private Predicate<Transfer> existsWithSameName = transfer -> getByNameAndNullAccount(transfer).hasNext();
    private Predicate<Transfer> existsWithSameAccount = transfer -> getByNameAndAccount(transfer).hasNext();

    @Autowired
    public PayeeDescriptorCollector(PayeeDescriptorRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void accept(Transfer transfer) {
        if (containsName.test(transfer)) {
            if (containsAccount.and(existsWithSameAccount.negate()).test(transfer))
                save(transfer);
            if (containsAccount.negate().and(existsWithSameName.negate()).test(transfer))
                save(transfer);
        }
    }

    private void save(Transfer transfer) {
        repository.save(PayeeDescriptor.builder()
                .fromAccount(transfer.getAccount())
                .transferType(transfer.getTransferType())
                .account(transfer.getPayeeAccount())
                .address(transfer.getPayeeAddress())
                .name(transfer.getPayeeName())
                .build()
        );
    }

    private Iterator<PayeeDescriptor> getByNameAndAccount(Transfer transfer) {
        return repository.findAll(
                payeeDescriptor.fromAccount.eq(transfer.getAccount())
                        .and(payeeDescriptor.transferType.eq(transfer.getTransferType()))
                        .and(payeeDescriptor.name.eq(transfer.getPayeeName()))
                        .and(payeeDescriptor.account.eq(transfer.getPayeeAccount()))
        ).iterator();
    }

    private Iterator<PayeeDescriptor> getByNameAndNullAccount(Transfer transfer) {
        return repository.findAll(
                payeeDescriptor.fromAccount.eq(transfer.getAccount())
                        .and(payeeDescriptor.transferType.eq(transfer.getTransferType()))
                        .and(payeeDescriptor.name.eq(transfer.getPayeeName()))
                        .and(payeeDescriptor.account.isNull())
        ).iterator();
    }

}
