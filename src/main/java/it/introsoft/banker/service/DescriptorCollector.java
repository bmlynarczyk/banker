package it.introsoft.banker.service;

import it.introsoft.banker.repository.CategoryDescriptor;
import it.introsoft.banker.repository.CategoryDescriptorRepository;
import it.introsoft.banker.repository.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static it.introsoft.banker.model.Bank.MILLENIUM;
import static it.introsoft.banker.model.transfer.type.TransferType.CARD_PAYMENT;
import static it.introsoft.banker.repository.DescriptorOrigin.CARD_PAYMENT_DESCRPTION;
import static it.introsoft.banker.repository.QCategoryDescriptor.categoryDescriptor;

@Component
public class DescriptorCollector implements Consumer<Transfer> {

    private static final Pattern milleniumCardPaymentDescriptionPattern = Pattern.compile("(.*) ([0-9][0-9]\\/[0-9][0-9]\\/[0-9][0-9])");
    private final CategoryDescriptorRepository repository;

    @Autowired
    public DescriptorCollector(CategoryDescriptorRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void accept(Transfer transfer) {
        if (MILLENIUM.equals(transfer.getBank()) && CARD_PAYMENT.equals(transfer.getTransferType())) {
            Matcher matcher = milleniumCardPaymentDescriptionPattern.matcher(transfer.getDescription());
            if (matcher.find() && !getByName(transfer, matcher.group(1)))
                saveCategoryDescriptor(transfer, matcher.group(1));
        }
    }

    private void saveCategoryDescriptor(Transfer transfer, String description) {
        repository.save(CategoryDescriptor.builder()
                .fromAccount(transfer.getAccount())
                .bank(transfer.getBank())
                .origin(CARD_PAYMENT_DESCRPTION)
                .transferType(transfer.getTransferType())
                .name(description)
                .category(null)
                .build()
        );
    }

    private boolean getByName(Transfer transfer, String description) {
        return repository.findAll(
                categoryDescriptor.fromAccount.eq(transfer.getAccount())
                        .and(categoryDescriptor.bank.eq(transfer.getBank()))
                        .and(categoryDescriptor.origin.eq(CARD_PAYMENT_DESCRPTION))
                        .and(categoryDescriptor.transferType.eq(transfer.getTransferType()))
                        .and(categoryDescriptor.name.eq(description))
        ).iterator().hasNext();
    }

}
