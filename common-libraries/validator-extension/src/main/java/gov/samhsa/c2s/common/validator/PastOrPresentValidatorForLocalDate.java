package gov.samhsa.c2s.common.validator;

import gov.samhsa.c2s.common.validator.constraint.PastOrPresent;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.spi.time.TimeProvider;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class PastOrPresentValidatorForLocalDate implements ConstraintValidator<PastOrPresent, LocalDate> {
    @Override
    public void initialize(PastOrPresent constraintAnnotation) {

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
        final LocalDate localDate = Instant.ofEpochMilli(now)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return localDate
                .isAfter(value) || localDate.isEqual(value);
    }
}