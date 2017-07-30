package it.introsoft.banker.controller;

import it.introsoft.banker.repository.TransferRepository;
import it.introsoft.banker.model.view.CategoriesReport;
import it.introsoft.banker.service.converter.CategoriesReportFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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
    public CategoriesReport getCategoriesReport(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date periodStart,
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date periodStop) {
        return categoriesReportFactory.convert(transferRepository.getSumByCategories(periodStart, periodStop));
    }

}
