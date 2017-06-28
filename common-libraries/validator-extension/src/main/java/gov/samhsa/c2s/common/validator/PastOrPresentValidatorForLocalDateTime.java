package gov.samhsa.c2s.common.validator;

import gov.samhsa.c2s.common.validator.constraint.PastOrPresent;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.spi.time.TimeProvider;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class PastOrPresentValidatorForLocalDateTime implements ConstraintValidator<PastOrPresent, LocalDateTime> {

    @Override
    public void initialize(PastOrPresent pastOrPresent) {

    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        TimeProvider timeProvider = constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class).getTimeProvider();
        long now = timeProvider.getCurrentTime();
        final LocalDateTime localDateTime = Instant.ofEpochMilli(now).atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime.isAfter(value) || localDateTime.isEqual(value);
    }
}

