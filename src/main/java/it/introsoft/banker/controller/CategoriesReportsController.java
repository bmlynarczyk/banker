package it.introsoft.banker.controller;

import it.introsoft.banker.model.view.CategoriesReport;
import it.introsoft.banker.model.view.CategorySum;
import it.introsoft.banker.repository.TransferRepository;
import it.introsoft.banker.service.converter.CategoriesReportFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@RestController
@RequestMapping("/api/categories/reports/")
@CrossOrigin
public class CategoriesReportsController {

    private final TransferRepository transferRepository;

    private final CategoriesReportFactory categoriesReportFactory;

    @Autowired
    public CategoriesReportsController(TransferRepository transferRepository, CategoriesReportFactory categoriesReportFactory) {
        this.transferRepository = transferRepository;
        this.categoriesReportFactory = categoriesReportFactory;
    }


    @GetMapping
    public CategoriesReport getCategoriesReport(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate periodStart,
                                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate periodStop) {
        List<CategorySum> categorySums = transferRepository.getSumByCategoriesExcludingCategories(periodStart, periodStop, newArrayList("wynagrodzenie"));
        return categoriesReportFactory.convert(categorySums);
    }

}
