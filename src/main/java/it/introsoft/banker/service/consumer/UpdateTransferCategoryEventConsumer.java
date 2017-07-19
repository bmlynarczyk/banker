package it.introsoft.banker.service.consumer;

import com.google.common.eventbus.Subscribe;
import it.introsoft.banker.model.Bank;
import it.introsoft.banker.repository.CategoryDescriptor;
import it.introsoft.banker.repository.CategoryDescriptorRepository;
import it.introsoft.banker.repository.TransferRepository;
import it.introsoft.banker.service.event.MilleniumUpdateTransferCategoryEvent;
import it.introsoft.banker.service.event.PkoBpUpdateTransferCategoryEvent;
import it.introsoft.banker.service.event.UnknownUpdateTransferCategoryEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static it.introsoft.banker.repository.QCategoryDescriptor.categoryDescriptor;

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
        for (CategoryDescriptor categoryDescriptor : getCategoryDescriptors(Bank.MILLENIUM)) {
            String descriptor = categoryDescriptor.getName();
            String category = categoryDescriptor.getCategory();
            switch (categoryDescriptor.getOrigin()) {
                case BENEFICIARY:
                    break;
                case PAYEE:
                    break;
                case CARD_PAYMENT_DESCRPTION:
                    transferRepository.setCategoryByDescriptionStartingWith(category, descriptor);
                    break;
            }
        }
    }

    @Subscribe
    public void accept(PkoBpUpdateTransferCategoryEvent event) {
        for (CategoryDescriptor categoryDescriptor : getCategoryDescriptors(Bank.PKO_BP)) {
            String descriptor = categoryDescriptor.getName();
            String category = categoryDescriptor.getCategory();
            switch (categoryDescriptor.getOrigin()) {
                case BENEFICIARY:
                    break;
                case PAYEE:
                    break;
                case CARD_PAYMENT_DESCRPTION:
                    transferRepository.setCategoryByDescriptionEndingWith(category, descriptor);
                    break;
            }
        }
    }

    @Subscribe
    public void accept(UnknownUpdateTransferCategoryEvent event) {
    }

    private Iterable<CategoryDescriptor> getCategoryDescriptors(Bank bank) {
        return categoryDescriptorRepository.findAll(categoryDescriptor.category.isNotNull().and(categoryDescriptor.bank.eq(bank)));
    }

}