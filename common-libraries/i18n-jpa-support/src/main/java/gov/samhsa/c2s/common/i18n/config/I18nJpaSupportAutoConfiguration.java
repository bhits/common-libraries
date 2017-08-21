package gov.samhsa.c2s.common.i18n.config;

import gov.samhsa.c2s.common.i18n.I18nEnabled;
import gov.samhsa.c2s.common.i18n.service.I18nService;
import gov.samhsa.c2s.common.i18n.service.I18nServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackageClasses = I18nEnabled.class)
public class I18nJpaSupportAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(I18nService.class)
    public I18nService i18nService() {
        return new I18nServiceImpl();
    }
}
