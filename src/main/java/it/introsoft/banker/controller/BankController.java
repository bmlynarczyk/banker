package it.introsoft.banker.controller;

import it.introsoft.banker.model.Bank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.toSet;

@Slf4j
@RestController
@RequestMapping("/api/banks")
@CrossOrigin
public class BankController {

    @GetMapping
    public Set<String> banks() {
        return newHashSet(Bank.values()).stream().map(Enum::toString).collect(toSet());
    }

}
