package it.introsoft.banker.service

import com.google.common.eventbus.EventBus
import it.introsoft.banker.model.Bank
import it.introsoft.banker.repository.CategoryDescriptor
import it.introsoft.banker.repository.CategoryDescriptorRepository
import it.introsoft.banker.service.consumer.UpdateCategoryDescriptorsEventConsumer
import it.introsoft.banker.service.event.UpdateCategoryDescriptorsEvent
import spock.lang.Specification

class UpdateCategoryDescriptorsEventConsumerTest extends Specification {

    Reader reader = new StringReader(
            '''
            a="a"
            ab="a b"
            abcd="a b c","d"
            ''');

    CategoryDescriptorRepository repository = Mock()
    EventBus eventBus = Mock()

    def "should correctly pars categories mappings"() {
        given:
        Properties properties = new Properties()
        properties.load(reader)
        UpdateCategoryDescriptorsEventConsumer consumer = new UpdateCategoryDescriptorsEventConsumer(repository, eventBus)
        when:
        consumer.accept(UpdateCategoryDescriptorsEvent.builder().bank(Bank.MILLENIUM).properties(properties).build())
        then:
        1 * repository.findAll(_) >> []
        0 * repository.save(_)
    }

    def "should correctly pars categories mappings and update matching"() {
        given:
        Properties properties = new Properties()
        properties.load(reader)
        UpdateCategoryDescriptorsEventConsumer consumer = new UpdateCategoryDescriptorsEventConsumer(repository, eventBus)
        when:
        consumer.accept(UpdateCategoryDescriptorsEvent.builder().bank(Bank.MILLENIUM).properties(properties).build())
        then:
        1 * repository.findAll(_) >> [CategoryDescriptor.builder().name("a b c").build()]
        1 * repository.save(_)
    }

}
