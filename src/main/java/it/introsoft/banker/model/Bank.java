package it.introsoft.banker.model;

import javax.validation.ValidationException;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.toSet;

public enum Bank {

    BGZ_OPTIMA("bgzoptima"),
    PKO_BP("pkobp"),
    M_BANK("mbank"),
    MILLENIUM("millenium");

    private final String name;

    Bank(String name) {
        this.name = name;
    }

    public static Bank of(String name) {
        for (Bank value : values()) {
            if (value.getName().equals(name))
                return value;
        }
        throw new ValidationException("Unknown bank");
    }

    public static Set<String> getNames() {
        return newHashSet(Bank.values()).stream().map(Bank::getName).collect(toSet());
    }

    public String getName() {
        return name;
    }

}