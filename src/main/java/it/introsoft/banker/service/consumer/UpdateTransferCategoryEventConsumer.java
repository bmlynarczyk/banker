package it.introsoft.banker.service.consumer;

import com.google.common.eventbus.Subscribe;
import it.introsoft.banker.model.jpa.CategoryDescriptor;
import it.introsoft.banker.model.raw.Bank;
import it.introsoft.banker.repository.CategoryDescriptorRepository;
import it.introsoft.banker.repository.TransferRepository;
import it.introsoft.banker.service.event.MilleniumUpdateTransferCategoryEvent;
import it.introsoft.banker.service.event.PkoBpUpdateTransferCategoryEvent;
import it.introsoft.banker.service.event.UnknownUpdateTransferCategoryEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static it.introsoft.banker.model.jpa.QCategoryDescriptor.categoryDescriptor;

@Slf4j
@Component
public class UpdateTransferCategoryEventConsumer {

    private final TransferRepository transferRepository;
    private final CategoryDescriptorRepository categoryDescriptorRepository;

    @Autowired
    public UpdateTransferCategoryEventConsumer(TransferRepository transferRepository,
                                               CategoryDescriptorRepository categoryDescriptorRepository) {
        this.transferRepository = transferRepository;
        this.categoryDescriptorRepository = categoryDescriptorRepository;
    }

    @Subscribe
    public void accept(MilleniumUpdateTransferCategoryEvent event) {
        long updatedTransfers = 0;
        for (CategoryDescriptor categoryDescriptor : getCategoryDescriptors(Bank.MILLENIUM)) {
            String descriptor = categoryDescriptor.getName();
            String category = categoryDescriptor.getCategory();
            switch (categoryDescriptor.getOrigin()) {
                case BENEFICIARY:
                    break;
                case PAYEE:
                    break;
                case CARD_PAYMENT_DESCRPTION:
                    updatedTransfers = updatedTransfers + transferRepository.setCategoryByDescriptionStartingWith(category, descriptor);
                    break;
            }
        }
        log.info("category updated in {} transfers", updatedTransfers);
    }

    @Subscribe
    public void accept(PkoBpUpdateTransferCategoryEvent event) {
        long updatedTransfers = 0;
        for (CategoryDescriptor categoryDescriptor : getCategoryDescriptors(Bank.PKO_BP)) {
            String descriptor = categoryDescriptor.getName();
            String category = categoryDescriptor.getCategory();
            switch (categoryDescriptor.getOrigin()) {
                case BENEFICIARY:
                    break;
                case PAYEE:
                    break;
                case CARD_PAYMENT_DESCRPTION:
                    updatedTransfers = updatedTransfers + transferRepository.setCategoryByDescriptionEndingWith(category, descriptor);
                    break;
            }
        }
        log.info("category updated in {} transfers", updatedTransfers);
    }

    @Subscribe
    public void accept(UnknownUpdateTransferCategoryEvent event) {
    }

    private Iterable<CategoryDescriptor> getCategoryDescriptors(Bank bank) {
        return categoryDescriptorRepository.findAll(categoryDescriptor.category.isNotNull().and(categoryDescriptor.bank.eq(bank)));
    }

}