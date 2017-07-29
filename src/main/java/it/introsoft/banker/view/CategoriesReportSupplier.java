package it.introsoft.banker.view;

import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import it.introsoft.banker.repository.TransferRepository;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
public class CategoriesReportSupplier implements Supplier<CategoriesReport> {

    private final Date periodStart;
    private final Date periodStop;
    private final String accountNumber;
    private final TransferRepository transferRepository;

    @Override
    public CategoriesReport get() {
        Set<String> categories = Sets.newHashSet();
        Map<String, Map<String, Object>> byMonth = Maps.newHashMap();
        transferRepository.getSumByCategories(accountNumber, periodStart, periodStop).forEach(categorySum -> {
            String month = categorySum.getYear() + "-" + (categorySum.getMonth() < 10 ? "0" : "") + categorySum.getMonth();
            byMonth.computeIfAbsent(month, s -> Maps.newHashMap());
            byMonth.get(month).put("month", month);
            byMonth.get(month).put(categorySum.getCategory(), categorySum.getTransferAmountSum());
            categories.add(categorySum.getCategory());
        });
        return CategoriesReport.builder().categories(categories).amountsByMonth(byMonth.values()).build();
    }
}
