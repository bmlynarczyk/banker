package it.introsoft.banker.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Properties;

@AllArgsConstructor
@Builder
@Getter
public class UpdateCategoryDescriptorsEvent {

    private final Properties properties;

}
