package it.introsoft.banker.model.view;

import lombok.Builder;
import lombok.Value;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Value
@Builder
public class CategoriesReport {

    Set<String> categories;

    Collection<Map<String, Object>> amountsByMonth;
}