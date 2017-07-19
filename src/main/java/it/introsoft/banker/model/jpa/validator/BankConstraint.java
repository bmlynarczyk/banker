package it.introsoft.banker.model.jpa.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BankValidator.class)
public @interface BankConstraint {
    String message() default "Unknown bank";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
