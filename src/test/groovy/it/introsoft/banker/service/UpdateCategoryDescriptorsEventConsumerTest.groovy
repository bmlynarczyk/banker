package it.introsoft.banker.service

import it.introsoft.banker.repository.CategoryDescriptor
import it.introsoft.banker.repository.CategoryDescriptorRepository
import spock.lang.Specification

class UpdateCategoryDescriptorsEventConsumerTest extends Specification {

    Reader reader = new StringReader(
            '''
            a="a"
            ab="a b"
            abcd="a b c","d"
            ''');

    CategoryDescriptorRepository repository = Mock()

    def "should correctly pars categories mappings"() {
        given:
        Properties properties = new Properties()
        properties.load(reader)
        UpdateCategoryDescriptorsEventConsumer consumer = new UpdateCategoryDescriptorsEventConsumer(repository)
        when:
        consumer.accept(UpdateCategoryDescriptorsEvent.builder().properties(properties).build())
        then:
        1 * repository.findAll(_) >> []
        0 * repository.save(_)
    }

    def "should correctly pars categories mappings and update matching"() {
        given:
        Properties properties = new Properties()
        properties.load(reader)
        UpdateCategoryDescriptorsEventConsumer consumer = new UpdateCategoryDescriptorsEventConsumer(repository)
        when:
        consumer.accept(UpdateCategoryDescriptorsEvent.builder().properties(properties).build())
        then:
        1 * repository.findAll(_) >> [CategoryDescriptor.builder().name("a b c").build()]
        1 * repository.save(_)
    }

}
