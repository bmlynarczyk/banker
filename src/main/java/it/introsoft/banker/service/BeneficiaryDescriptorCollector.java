package it.introsoft.banker.service;

import it.introsoft.banker.repository.BeneficiaryDescriptor;
import it.introsoft.banker.repository.BeneficiaryDescriptorRepository;
import it.introsoft.banker.repository.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static it.introsoft.banker.repository.QBeneficiaryDescriptor.beneficiaryDescriptor;

@Component
public class BeneficiaryDescriptorCollector implements Consumer<Transfer> {
    private final BeneficiaryDescriptorRepository repository;

    private Predicate<Transfer> containsBeneficiaryName = transfer -> null != transfer.getBeneficiaryName();
    private Predicate<Transfer> containsBeneficiaryAccount = transfer -> null != transfer.getBeneficiaryAccount();
    private Predicate<Transfer> existsWithSameBeneficiaryName = transfer -> getByBeneficiaryNameAndNullBeneficiaryAccount(transfer).hasNext();
    private Predicate<Transfer> existsWithSameBeneficiaryAccount = transfer -> getByBeneficiaryNameAndBeneficiaryAccount(transfer).hasNext();

    @Autowired
    public BeneficiaryDescriptorCollector(BeneficiaryDescriptorRepository repository) {
        this.repository = repository;
    }

    @Override
    public void accept(Transfer transfer) {
        if (containsBeneficiaryName.test(transfer)) {
            if (containsBeneficiaryAccount.and(existsWithSameBeneficiaryAccount.negate()).test(transfer))
                saveBeneficiaryTransferDescriptor(transfer);
            else if (existsWithSameBeneficiaryName.negate().test(transfer))
                saveBeneficiaryTransferDescriptor(transfer);
        }
    }

    private void saveBeneficiaryTransferDescriptor(Transfer transfer) {
        repository.save(BeneficiaryDescriptor.builder()
                .forAccount(transfer.getAccount())
                .transferType(transfer.getTransferType())
                .account(transfer.getBeneficiaryAccount())
                .address(transfer.getBeneficiaryAddress())
                .name(transfer.getBeneficiaryName())
                .build()
        );
    }

    private Iterator<BeneficiaryDescriptor> getByBeneficiaryNameAndBeneficiaryAccount(Transfer transfer) {
        return repository.findAll(
                beneficiaryDescriptor.forAccount.eq(transfer.getAccount())
                        .and(beneficiaryDescriptor.transferType.eq(transfer.getTransferType()))
                        .and(beneficiaryDescriptor.name.eq(transfer.getBeneficiaryName()))
                        .and(beneficiaryDescriptor.account.eq(transfer.getBeneficiaryAccount()))
        ).iterator();
    }

    private Iterator<BeneficiaryDescriptor> getByBeneficiaryNameAndNullBeneficiaryAccount(Transfer transfer) {
        return repository.findAll(
                beneficiaryDescriptor.forAccount.eq(transfer.getAccount())
                        .and(beneficiaryDescriptor.transferType.eq(transfer.getTransferType()))
                        .and(beneficiaryDescriptor.name.eq(transfer.getBeneficiaryName()))
                        .and(beneficiaryDescriptor.account.isNull())
        ).iterator();
    }

}
