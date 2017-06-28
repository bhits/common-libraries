package gov.samhsa.c2s.common.validator;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.spi.time.TimeProvider;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Future;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class FutureValidatorForLocalDateTime implements ConstraintValidator<Future, LocalDateTime> {
    @Override
    public void initialize(Future constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        TimeProvider timeProvider = constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class).getTimeProvider();
        long now = timeProvider.getCurrentTime();
        return Instant.ofEpochMilli(now).atZone(ZoneId.systemDefault()).toLocalDateTime().isBefore(value);
    }

}