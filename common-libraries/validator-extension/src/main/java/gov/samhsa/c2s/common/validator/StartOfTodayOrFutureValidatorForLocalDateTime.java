package gov.samhsa.c2s.common.validator;

import gov.samhsa.c2s.common.validator.constraint.StartOfTodayOrFuture;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.spi.time.TimeProvider;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class StartOfTodayOrFutureValidatorForLocalDateTime implements ConstraintValidator<StartOfTodayOrFuture, LocalDateTime> {
    @Override
    public void initialize(StartOfTodayOrFuture startOfTodayOrFuture) {

    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        TimeProvider timeProvider = constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class).getTimeProvider();
        long now = timeProvider.getCurrentTime();
        final LocalDate localDate = Instant.ofEpochMilli(now)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        final LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.MIN);
        return localDateTime.isBefore(value) || localDateTime.isEqual(value);
    }
}
