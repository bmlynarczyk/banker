package it.introsoft.banker.service.converter;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import it.introsoft.banker.model.view.CategoriesReport;
import it.introsoft.banker.model.view.CategorySum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class CategoriesReportFactory implements Converter<List<CategorySum>, CategoriesReport> {

    @Override
    public CategoriesReport convert(List<CategorySum> categorySums) {
        Set<String> categories = Sets.newHashSet();
        Map<String, Map<String, Object>> byMonth = Maps.newHashMap();
        categorySums.forEach(categorySum -> {
            String month = categorySum.getYear() + "-" + (categorySum.getMonth() < 10 ? "0" : "") + categorySum.getMonth();
            byMonth.computeIfAbsent(month, s -> Maps.newHashMap());
            byMonth.get(month).put("month", month);
            byMonth.get(month).put(categorySum.getCategory(), categorySum.getTransferAmountSum());
            categories.add(categorySum.getCategory());
        });
        return CategoriesReport.builder().categories(categories).amountsByMonth(byMonth.values()).build();
    }

}
