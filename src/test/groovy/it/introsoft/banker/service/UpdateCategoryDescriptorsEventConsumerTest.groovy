package it.introsoft.banker.service

import com.google.common.eventbus.EventBus
import it.introsoft.banker.model.jpa.CategoryDescriptor
import it.introsoft.banker.model.raw.Bank
import it.introsoft.banker.repository.CategoryDescriptorRepository
import it.introsoft.banker.service.consumer.UpdateCategoryDescriptorsEventConsumer
import it.introsoft.banker.service.event.UpdateCategoryDescriptorsEvent
import org.jopendocument.dom.spreadsheet.Sheet
import org.jopendocument.dom.spreadsheet.SpreadSheet
import spock.lang.Specification

class UpdateCategoryDescriptorsEventConsumerTest extends Specification {

    CategoryDescriptorRepository repository = Mock()
    EventBus eventBus = Mock()

    def "should correctly pars categories mappings"() {
        given:
        Sheet sheet = SpreadSheet.createFromFile((new File('src/test/resources/test-cat-map.ods'))).getSheet(0);
        UpdateCategoryDescriptorsEventConsumer consumer = new UpdateCategoryDescriptorsEventConsumer(repository, eventBus)
        when:
        consumer.accept(UpdateCategoryDescriptorsEvent.builder().bank(Bank.MILLENIUM).sheet(sheet).build())
        then:
        1 * repository.findAll(_) >> []
        0 * repository.save(_)
    }

    def "should correctly pars categories mappings and update matching"() {
        given:
        Sheet sheet = SpreadSheet.createFromFile((new File('src/test/resources/test-cat-map.ods'))).getSheet(0);
        UpdateCategoryDescriptorsEventConsumer consumer = new UpdateCategoryDescriptorsEventConsumer(repository, eventBus)
        when:
        consumer.accept(UpdateCategoryDescriptorsEvent.builder().bank(Bank.MILLENIUM).sheet(sheet).build())
        then:
        1 * repository.findAll(_) >> [CategoryDescriptor.builder().name("a b c").build()]
        1 * repository.save(_)
    }

}
