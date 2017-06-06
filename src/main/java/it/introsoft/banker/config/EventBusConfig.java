package it.introsoft.banker.config;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import it.introsoft.banker.service.UpdateAccountEventConsumer;
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
    public EventBus eventBus(ThreadPoolTaskExecutor eventBusExecutor, UpdateAccountEventConsumer updateAccountEventConsumer) {
        EventBus eventBus = new AsyncEventBus(eventBusExecutor);
        eventBus.register(updateAccountEventConsumer);
        return eventBus;
    }

}
