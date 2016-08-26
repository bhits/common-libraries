package gov.samhsa.c2s.common.validator;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.spi.time.TimeProvider;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Future;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class FutureValidatorForLocalDate implements ConstraintValidator<Future, LocalDate> {
    @Override
    public void initialize(Future constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        // null values are valid
        if (value == null) {
            return true;
        }
        TimeProvider timeProvider = context.unwrap(HibernateConstraintValidatorContext.class)
                .getTimeProvider();
        long now = timeProvider.getCurrentTime();
        return Instant.ofEpochMilli(now)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .isBefore(value);
    }
}
