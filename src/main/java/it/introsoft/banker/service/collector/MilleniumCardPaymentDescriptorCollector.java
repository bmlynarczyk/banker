package it.introsoft.banker.service.collector;

import it.introsoft.banker.model.jpa.Transfer;
import it.introsoft.banker.repository.CategoryDescriptorRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static it.introsoft.banker.model.raw.TransferType.CARD_PAYMENT;

public class MilleniumCardPaymentDescriptorCollector extends BaseCardPaymentDescriptorCollector {

    private static final Pattern descriptionPattern = Pattern.compile("(.*) ([0-9][0-9]\\/[0-9][0-9]\\/[0-9][0-9])");

    MilleniumCardPaymentDescriptorCollector(CategoryDescriptorRepository repository) {
        super(repository);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void accept(Transfer transfer) {
        if (CARD_PAYMENT.equals(transfer.getTransferType())) {
            Matcher matcher = descriptionPattern.matcher(transfer.getDescription());
            if (matcher.find() && !getByName(transfer, matcher.group(1)))
                saveCategoryDescriptor(transfer, matcher.group(1));
        }
    }

}
