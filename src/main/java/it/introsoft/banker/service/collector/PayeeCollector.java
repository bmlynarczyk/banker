package it.introsoft.banker.service.collector;

import it.introsoft.banker.repository.CategoryDescriptor;
import it.introsoft.banker.repository.CategoryDescriptorRepository;
import it.introsoft.banker.repository.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static it.introsoft.banker.repository.DescriptorOrigin.PAYEE;
import static it.introsoft.banker.repository.QCategoryDescriptor.categoryDescriptor;

@Component
public class PayeeCollector implements Consumer<Transfer> {

    private final CategoryDescriptorRepository repository;

    private Predicate<Transfer> containsName = transfer -> null != transfer.getPayeeName();
    private Predicate<Transfer> containsAccount = transfer -> null != transfer.getPayeeAccount();
    private Predicate<Transfer> existsWithSameName = transfer -> getByNameAndNullAccount(transfer).hasNext();
    private Predicate<Transfer> existsWithSameAccount = transfer -> getByNameAndAccount(transfer).hasNext();

    @Autowired
    public PayeeCollector(CategoryDescriptorRepository repository) {
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
                .origin(PAYEE)
                .bank(transfer.getBank())
                .fromAccount(transfer.getAccount())
                .transferType(transfer.getTransferType())
                .account(transfer.getPayeeAccount())
                .address(transfer.getPayeeAddress())
                .name(transfer.getPayeeName())
                .build()
        );
    }

    private Iterator<CategoryDescriptor> getByNameAndAccount(Transfer transfer) {
        return repository.findAll(
                categoryDescriptor.fromAccount.eq(transfer.getAccount())
                        .and(categoryDescriptor.origin.eq(PAYEE))
                        .and(categoryDescriptor.transferType.eq(transfer.getTransferType()))
                        .and(categoryDescriptor.name.eq(transfer.getPayeeName()))
                        .and(categoryDescriptor.account.eq(transfer.getPayeeAccount()))
        ).iterator();
    }

    private Iterator<CategoryDescriptor> getByNameAndNullAccount(Transfer transfer) {
        return repository.findAll(
                categoryDescriptor.fromAccount.eq(transfer.getAccount())
                        .and(categoryDescriptor.origin.eq(PAYEE))
                        .and(categoryDescriptor.transferType.eq(transfer.getTransferType()))
                        .and(categoryDescriptor.name.eq(transfer.getPayeeName()))
                        .and(categoryDescriptor.account.isNull())
        ).iterator();
    }

}
