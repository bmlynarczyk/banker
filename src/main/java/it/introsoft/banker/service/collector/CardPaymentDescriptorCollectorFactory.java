package it.introsoft.banker.service.collector;

import it.introsoft.banker.model.raw.Bank;
import it.introsoft.banker.repository.CategoryDescriptorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CardPaymentDescriptorCollectorFactory {

    private final CategoryDescriptorRepository repository;

    private final EmptyCardPaymentDescriptorCollector emptyCollector = new EmptyCardPaymentDescriptorCollector();

    @Autowired
    public CardPaymentDescriptorCollectorFactory(CategoryDescriptorRepository categoryDescriptorRepository) {
        this.repository = categoryDescriptorRepository;
    }

    public CardPaymentDescriptorCollector get(Bank bank) {
        switch (bank) {
            case BGZ_OPTIMA:
                return emptyCollector;
            case PKO_BP:
                return new PkoBpCardPaymentDescriptorCollector(repository);
            case M_BANK:
                return emptyCollector;
            case MILLENIUM:
                return new MilleniumCardPaymentDescriptorCollector(repository);
            case BZ_WBK:
                return new BzWbkCardPaymentDescriptorCollector(repository);
            default:
                throw new IllegalArgumentException("Unknown bank");
        }
    }


}
