package gov.samhsa.c2s.common.i18n.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import(I18nJpaSupportAutoConfiguration.class)
public @interface EnableI18nJpaSupport {
}
