package it.introsoft.banker.service

import groovy.util.logging.Slf4j
import it.introsoft.banker.model.jpa.Transfer
import it.introsoft.banker.repository.CategoryDescriptorRepository
import it.introsoft.banker.service.collector.CardPaymentDescriptorCollectorFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import static it.introsoft.banker.model.jpa.QCategoryDescriptor.categoryDescriptor
import static it.introsoft.banker.model.raw.Bank.MILLENIUM
import static it.introsoft.banker.model.raw.TransferType.CARD_PAYMENT
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
@Slf4j
@ContextConfiguration
class MilleniumCardPaymentDescriptorCollectorTest extends Specification {

    @Autowired
    CardPaymentDescriptorCollectorFactory descriptorCollectorFactory

    @Autowired
    CategoryDescriptorRepository repository

    def "should save extracted category descriptor for millenium bank"() {
        given:
        Transfer transfer = Transfer.builder()
                .bank(MILLENIUM)
                .account("98116022020000000000000000")
                .transferType(CARD_PAYMENT)
                .description(given)
                .build()
        when:
        descriptorCollectorFactory.get(MILLENIUM).accept(transfer)
        then:
        repository.exists(categoryDescriptor.name.eq(expected))
        where:
        given                                                                            | expected
        "Stolowka Lublin 17/06/27"                                                       | "Stolowka Lublin"
        "E LECLERC LUBLIN 16/09/154800000000000000 ZL 10,06 : 005055983786"              | "E LECLERC LUBLIN"
        "Luxmed Lublin ul. RadziLublin 16/10/254874742095022952 ZL450,00 : 005153103885" | "Luxmed Lublin ul. RadziLublin"
    }

    def "shouldn't save existing category descriptor"() {
        given:
        Transfer transfer = Transfer.builder()
                .bank(MILLENIUM)
                .account("98116022020000000000000000")
                .transferType(CARD_PAYMENT)
                .description("LPP RESERVED 1912070 LUBLIN 17/06/28")
                .build()
        when:
        descriptorCollectorFactory.get(MILLENIUM).accept(transfer)
        descriptorCollectorFactory.get(MILLENIUM).accept(transfer)
        then:
        repository.findAll(categoryDescriptor.name.eq("LPP RESERVED 1912070 LUBLIN")).size() == 1
    }

}
