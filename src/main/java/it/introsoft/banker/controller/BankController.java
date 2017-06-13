package it.introsoft.banker.controller;

import it.introsoft.banker.model.Bank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/banks")
@CrossOrigin
public class BankController {

    @GetMapping
    public Set<String> banks() {
        return Bank.getNames();
    }

}
