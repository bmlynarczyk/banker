package it.introsoft.banker.config;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import it.introsoft.banker.service.consumer.UpdateAccountBalanceEventConsumer;
import it.introsoft.banker.service.consumer.UpdateCategoryDescriptorsEventConsumer;
import it.introsoft.banker.service.consumer.UpdateTransferCategoryEventConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class EventBusConfig {

    @Bean
    ThreadPoolTaskExecutor eventBusExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean
    public EventBus eventBus(ThreadPoolTaskExecutor eventBusExecutor,
                             UpdateAccountBalanceEventConsumer accountEventConsumer,
                             UpdateCategoryDescriptorsEventConsumer descriptorCategoryEventConsumer,
                             UpdateTransferCategoryEventConsumer transferCategoryEventConsumer) {
        EventBus eventBus = new AsyncEventBus(eventBusExecutor);
        eventBus.register(accountEventConsumer);
        eventBus.register(descriptorCategoryEventConsumer);
        eventBus.register(transferCategoryEventConsumer);
        return eventBus;
    }

}
