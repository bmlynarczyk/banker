package it.introsoft.banker.service.collector;

import it.introsoft.banker.model.jpa.CategoryDescriptor;
import it.introsoft.banker.model.jpa.Transfer;
import it.introsoft.banker.repository.CategoryDescriptorRepository;

import static it.introsoft.banker.model.jpa.DescriptorOrigin.CARD_PAYMENT_DESCRIPTION;
import static it.introsoft.banker.model.jpa.QCategoryDescriptor.categoryDescriptor;

abstract class BaseCardPaymentDescriptorCollector implements CardPaymentDescriptorCollector {

    private final CategoryDescriptorRepository repository;

    BaseCardPaymentDescriptorCollector(CategoryDescriptorRepository repository) {
        this.repository = repository;
    }

    void saveCategoryDescriptor(Transfer transfer, String description) {
        repository.save(CategoryDescriptor.builder()
                .fromAccount(transfer.getAccount())
                .bank(transfer.getBank())
                .origin(CARD_PAYMENT_DESCRIPTION)
                .transferType(transfer.getTransferType())
                .name(description)
                .category(null)
                .build()
        );
    }

    boolean getByName(Transfer transfer, String description) {
        return repository.findAll(
                categoryDescriptor.fromAccount.eq(transfer.getAccount())
                        .and(categoryDescriptor.bank.eq(transfer.getBank()))
                        .and(categoryDescriptor.origin.eq(CARD_PAYMENT_DESCRIPTION))
                        .and(categoryDescriptor.transferType.eq(transfer.getTransferType()))
                        .and(categoryDescriptor.name.eq(description))
        ).iterator().hasNext();
    }

}
