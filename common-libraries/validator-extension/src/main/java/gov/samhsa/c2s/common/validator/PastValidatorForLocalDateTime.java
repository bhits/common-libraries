package gov.samhsa.c2s.common.validator;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.hibernate.validator.spi.time.TimeProvider;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Past;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class PastValidatorForLocalDateTime implements ConstraintValidator<Past,LocalDateTime>{
    @Override
    public void initialize(Past past) {

    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext constraintValidatorContext) {
        TimeProvider timeProvider= constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class).getTimeProvider();
        long now=timeProvider.getCurrentTime();
        return Instant.ofEpochMilli(now).atZone(ZoneId.systemDefault()).toLocalDateTime().isAfter(value);
    }
}