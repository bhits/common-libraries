package gov.samhsa.c2s.common.validator;

import gov.samhsa.c2s.common.validator.constraint.PresentOrFuture;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.spi.time.TimeProvider;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class PresentOrFutureValidatorForLocalDateTime implements ConstraintValidator<PresentOrFuture, LocalDateTime>{
    @Override
    public void initialize(PresentOrFuture presentOrFuture) {

    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext constraintValidatorContext) {
        if(value ==null){
            return true;
        }
        TimeProvider timeProvider=constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class).getTimeProvider();
        long now=timeProvider.getCurrentTime();
        final LocalDateTime localDateTime = Instant.ofEpochMilli(now)
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime.isBefore(value) || localDateTime.isEqual(value);
    }
}