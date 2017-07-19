package it.introsoft.banker.service.consumer;

import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import it.introsoft.banker.repository.CategoryDescriptor;
import it.introsoft.banker.repository.CategoryDescriptorRepository;
import it.introsoft.banker.service.event.UpdateCategoryDescriptorsEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;

import static it.introsoft.banker.repository.QCategoryDescriptor.categoryDescriptor;

@Slf4j
@Component
public class UpdateCategoryDescriptorsEventConsumer implements Consumer<UpdateCategoryDescriptorsEvent> {

    private final CategoryDescriptorRepository descriptorRepository;

    private final EventBus eventBus;

    @Autowired
    public UpdateCategoryDescriptorsEventConsumer(CategoryDescriptorRepository descriptorRepository, @Lazy EventBus eventBus) {
        this.descriptorRepository = descriptorRepository;
        this.eventBus = eventBus;
    }

    private String[] parseDescriptors(String descriptors) {
        return descriptors.substring(1, descriptors.length() - 1).split("\",\"");
    }


    @Subscribe
    @Override
    public void accept(UpdateCategoryDescriptorsEvent event) {

        Map<String, String> categoryMappings = getCategoryMappings(event.getProperties());

        int updatedDescriptorsCount = 0;

        for (CategoryDescriptor categoryDescriptor : getCategoryDescriptors()) {
            String descriptor = categoryDescriptor.getName();
            String category = categoryMappings.get(descriptor);
            if (category != null) {
                updatedDescriptorsCount = updatedDescriptorsCount + 1;
                categoryDescriptor.setCategory(category);
                descriptorRepository.save(categoryDescriptor);
            }
        }

        log.info("count of updated category descriptors {}", updatedDescriptorsCount);
        eventBus.post(event.getBank().getUpdateTransferCategoryEvent());
    }

    private Map<String, String> getCategoryMappings(Properties properties) {
        Map<String, String> categoryMapping = Maps.newHashMap();
        properties.stringPropertyNames().forEach(category -> {
            String descriptors = properties.getProperty(category);
            if (descriptors.length() < 3 || !descriptors.startsWith("\"") || !descriptors.endsWith("\""))
                throw new IllegalArgumentException("value of property " + category + "is invalid");
            for (String descriptor : parseDescriptors(descriptors)) {
                categoryMapping.put(descriptor, category);
            }
        });
        log.info("category mapping size {}", categoryMapping.size());
        return categoryMapping;
    }

    private Iterable<CategoryDescriptor> getCategoryDescriptors() {
        return descriptorRepository.findAll(categoryDescriptor.category.isNull());
    }

}