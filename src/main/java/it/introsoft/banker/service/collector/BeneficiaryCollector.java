package it.introsoft.banker.service.collector;

import it.introsoft.banker.model.jpa.CategoryDescriptor;
import it.introsoft.banker.model.jpa.DescriptorOrigin;
import it.introsoft.banker.model.jpa.Transfer;
import it.introsoft.banker.repository.CategoryDescriptorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static it.introsoft.banker.model.jpa.DescriptorOrigin.BENEFICIARY;
import static it.introsoft.banker.model.jpa.QCategoryDescriptor.categoryDescriptor;

@Component
public class BeneficiaryCollector implements Consumer<Transfer> {
    private final CategoryDescriptorRepository repository;

    private Predicate<Transfer> containsName = transfer -> null != transfer.getBeneficiaryName();
    private Predicate<Transfer> containsAccount = transfer -> null != transfer.getBeneficiaryAccount();
    private Predicate<Transfer> existsWithSameName = transfer -> getByNameAndNullAccount(transfer).hasNext();
    private Predicate<Transfer> existsWithSameAccount = transfer -> getByNameAndAccount(transfer).hasNext();

    @Autowired
    public BeneficiaryCollector(CategoryDescriptorRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void accept(Transfer transfer) {
        if (containsName.test(transfer)) {
            if (containsAccount.and(existsWithSameAccount.negate()).test(transfer))
                saveCategoryDescriptor(transfer);
            if (containsAccount.negate().and(existsWithSameName.negate()).test(transfer))
                saveCategoryDescriptor(transfer);
        }
    }

    private void saveCategoryDescriptor(Transfer transfer) {
        repository.save(CategoryDescriptor.builder()
                .origin(DescriptorOrigin.BENEFICIARY)
                .fromAccount(transfer.getAccount())
                .transferType(transfer.getTransferType())
                .account(transfer.getBeneficiaryAccount())
                .address(transfer.getBeneficiaryAddress())
                .name(transfer.getBeneficiaryName())
                .bank(transfer.getBank())
                .build()
        );
    }

    private Iterator<CategoryDescriptor> getByNameAndAccount(Transfer transfer) {
        return repository.findAll(
                categoryDescriptor.fromAccount.eq(transfer.getAccount())
                        .and(categoryDescriptor.origin.eq(BENEFICIARY))
                        .and(categoryDescriptor.transferType.eq(transfer.getTransferType()))
                        .and(categoryDescriptor.name.eq(transfer.getBeneficiaryName()))
                        .and(categoryDescriptor.account.eq(transfer.getBeneficiaryAccount()))
        ).iterator();
    }

    private Iterator<CategoryDescriptor> getByNameAndNullAccount(Transfer transfer) {
        return repository.findAll(
                categoryDescriptor.fromAccount.eq(transfer.getAccount())
                        .and(categoryDescriptor.origin.eq(BENEFICIARY))
                        .and(categoryDescriptor.transferType.eq(transfer.getTransferType()))
                        .and(categoryDescriptor.name.eq(transfer.getBeneficiaryName()))
                        .and(categoryDescriptor.account.isNull())
        ).iterator();
    }

}
