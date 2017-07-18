package it.introsoft.banker.service;

import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;
import it.introsoft.banker.repository.CategoryDescriptor;
import it.introsoft.banker.repository.CategoryDescriptorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;

import static it.introsoft.banker.repository.QCategoryDescriptor.categoryDescriptor;

@Slf4j
@Component
public class UpdateCategoryDescriptorsEventConsumer implements Consumer<UpdateCategoryDescriptorsEvent> {

    private final CategoryDescriptorRepository descriptorRepository;

    @Autowired
    public UpdateCategoryDescriptorsEventConsumer(CategoryDescriptorRepository descriptorRepository) {
        this.descriptorRepository = descriptorRepository;
    }

    private String[] parseDescriptors(String descriptors) {
        return descriptors.substring(1, descriptors.length() - 1).split("\",\"");
    }


    @Subscribe
    @Override
    public void accept(UpdateCategoryDescriptorsEvent event) {

        Map<String, String> categoryMappings = getCategoryMappings(event);

        int updatedDescriptorsCount = 0;

        for (CategoryDescriptor categoryDescriptor : getCategoryDescriptors()) {
            String descriptor = categoryDescriptor.getName();
            String category = categoryMappings.get(descriptor.toUpperCase());
            if (category != null) {
                updatedDescriptorsCount = updatedDescriptorsCount + 1;
                categoryDescriptor.setCategory(category);
                descriptorRepository.save(categoryDescriptor);
            }
        }

        log.info("count of updated category descriptors {}", updatedDescriptorsCount);
    }

    private Map<String, String> getCategoryMappings(UpdateCategoryDescriptorsEvent event) {
        Map<String, String> categoryMapping = Maps.newHashMap();
        Properties properties = event.getProperties();
        properties.stringPropertyNames().forEach(category -> {
            String descriptors = properties.getProperty(category);
            if (descriptors.length() < 3 || !descriptors.startsWith("\"") || !descriptors.endsWith("\""))
                throw new IllegalArgumentException("value of property " + category + "is invalid");
            for (String descriptor : parseDescriptors(descriptors)) {
                categoryMapping.put(descriptor.toUpperCase(), category);
            }
        });
        log.info("category mapping size {}", categoryMapping.size());
        return categoryMapping;
    }

    private Iterable<CategoryDescriptor> getCategoryDescriptors() {
        return descriptorRepository.findAll(categoryDescriptor.category.isNull());
    }

}