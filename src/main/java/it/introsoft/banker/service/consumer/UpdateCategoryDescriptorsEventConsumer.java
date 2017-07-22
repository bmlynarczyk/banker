package it.introsoft.banker.service.consumer;

import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import it.introsoft.banker.model.jpa.CategoryDescriptor;
import it.introsoft.banker.repository.CategoryDescriptorRepository;
import it.introsoft.banker.service.event.UpdateCategoryDescriptorsEvent;
import lombok.extern.slf4j.Slf4j;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Consumer;

import static it.introsoft.banker.model.jpa.QCategoryDescriptor.categoryDescriptor;

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

        Map<String, String> categoryMappings = getCategoryMappings(event.getSheet());

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

    private Map<String, String> getCategoryMappings(Sheet sheet) {
        Map<String, String> categoryMapping = Maps.newHashMap();
        for (int i = 0; i < sheet.getRowCount(); i++) {
            String descriptor = sheet.getValueAt(0, i).toString();
            String category = sheet.getValueAt(1, i).toString();
            if (!descriptor.isEmpty())
                categoryMapping.put(descriptor, category);
        }
        return categoryMapping;
    }

    private Iterable<CategoryDescriptor> getCategoryDescriptors() {
        return descriptorRepository.findAll(categoryDescriptor.category.isNull());
    }

}