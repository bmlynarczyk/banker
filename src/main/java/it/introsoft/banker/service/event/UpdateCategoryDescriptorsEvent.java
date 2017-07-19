package it.introsoft.banker.service.event;

import it.introsoft.banker.model.Bank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Properties;

@AllArgsConstructor
@Builder
@Getter
public class UpdateCategoryDescriptorsEvent {

    private final Bank bank;

    private final Properties properties;

}
