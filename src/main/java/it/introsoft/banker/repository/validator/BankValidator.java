package it.introsoft.banker.repository.validator;

import it.introsoft.banker.model.Bank;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.stream.Collectors.toSet;

public class BankValidator implements ConstraintValidator<BankConstraint, Bank> {

    @Override
    public void initialize(BankConstraint bankConstraint) {
    }

    @Override
    public boolean isValid(Bank bank, ConstraintValidatorContext constraintValidatorContext) {
        return newHashSet(Bank.values()).stream().map(Enum::toString).collect(toSet()).contains(bank.name());
    }

}
