package it.introsoft.banker.service.consumer;

import com.google.common.eventbus.Subscribe;
import it.introsoft.banker.model.jpa.CategoryDescriptor;
import it.introsoft.banker.model.jpa.DescriptorOrigin;
import it.introsoft.banker.model.raw.Bank;
import it.introsoft.banker.repository.CategoryDescriptorRepository;
import it.introsoft.banker.repository.TransferRepository;
import it.introsoft.banker.service.event.BzWbkUpdateTransferCategoryEvent;
import it.introsoft.banker.service.event.MilleniumUpdateTransferCategoryEvent;
import it.introsoft.banker.service.event.PkoBpUpdateTransferCategoryEvent;
import it.introsoft.banker.service.event.UnknownUpdateTransferCategoryEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.google.common.collect.Streams.stream;
import static it.introsoft.banker.model.jpa.DescriptorOrigin.*;
import static it.introsoft.banker.model.jpa.QCategoryDescriptor.categoryDescriptor;
import static it.introsoft.banker.model.raw.Bank.*;

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

        updatedTransfers += updateMilleniumCardPaymentCategories();
        updatedTransfers += updateTransferCategoriesByBeneficiary(MILLENIUM);
        updatedTransfers += updateTransferCategoriesByPayee(MILLENIUM);

        log.info("category updated in {} transfers", updatedTransfers);
    }

    @Subscribe
    public void accept(PkoBpUpdateTransferCategoryEvent event) {
        long updatedTransfers = 0;

        updatedTransfers += updatePkoBpCardPaymentCategories();
        updatedTransfers += updateTransferCategoriesByBeneficiary(PKO_BP);
        updatedTransfers += updateTransferCategoriesByPayee(PKO_BP);

        log.info("category updated in {} transfers", updatedTransfers);
    }

    @Subscribe
    public void accept(BzWbkUpdateTransferCategoryEvent event) {
        long updatedTransfers = 0;

        updatedTransfers += updateBzWbkCardPaymentCategories();
        updatedTransfers += updateTransferCategoriesByBeneficiary(BZ_WBK);
        updatedTransfers += updateTransferCategoriesByPayee(BZ_WBK);

        log.info("category updated in {} transfers", updatedTransfers);
    }

    @Subscribe
    public void accept(UnknownUpdateTransferCategoryEvent event) {
    }

    private long updateMilleniumCardPaymentCategories() {
        return stream(getCategoryDescriptors(MILLENIUM, CARD_PAYMENT_DESCRIPTION))
                .mapToLong(cd -> transferRepository.setCategoryByDescriptionStartingWith(
                        cd.getCategory(), cd.getName(), cd.getTransferType(), cd.getBank()
                )).sum();
    }

    private long updatePkoBpCardPaymentCategories() {
        return stream(getCategoryDescriptors(PKO_BP, CARD_PAYMENT_DESCRIPTION))
                .mapToLong(cd -> transferRepository.setCategoryByDescriptionEndingWith(
                        cd.getCategory(), cd.getName(), cd.getTransferType(), cd.getBank()
                )).sum();
    }

    private long updateBzWbkCardPaymentCategories() {
        return stream(getCategoryDescriptors(BZ_WBK, CARD_PAYMENT_DESCRIPTION))
                .mapToLong(cd -> transferRepository.setCategoryByDescriptionEndingWith(
                        cd.getCategory(), cd.getName(), cd.getTransferType(), cd.getBank()
                )).sum();
    }

    private long updateTransferCategoriesByBeneficiary(Bank bank) {
        return stream(getCategoryDescriptors(bank, BENEFICIARY))
                .mapToLong(cd -> transferRepository.setCategoryByBeneficiary(
                        cd.getCategory(), cd.getName(), cd.getTransferType(), cd.getBank()
                )).sum();
    }

    private long updateTransferCategoriesByPayee(Bank bank) {
        return stream(getCategoryDescriptors(bank, PAYEE))
                .mapToLong(cd -> transferRepository.setCategoryByPayee(
                        cd.getCategory(), cd.getName(), cd.getTransferType(), cd.getBank()
                )).sum();
    }

    private Iterable<CategoryDescriptor> getCategoryDescriptors(Bank bank, DescriptorOrigin descriptorOrigin) {
        return categoryDescriptorRepository.findAll(categoryDescriptor.category.isNotNull()
                .and(categoryDescriptor.bank.eq(bank))
                .and(categoryDescriptor.origin.eq(descriptorOrigin))
        );
    }

}