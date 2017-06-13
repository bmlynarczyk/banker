package it.introsoft.banker.repository.validator;

import it.introsoft.banker.model.Bank;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BankValidator implements ConstraintValidator<BankConstraint, String> {

    @Override
    public void initialize(BankConstraint bankConstraint) {
    }

    @Override
    public boolean isValid(String bank, ConstraintValidatorContext constraintValidatorContext) {
        return Bank.getNames().contains(bank);
    }

}
