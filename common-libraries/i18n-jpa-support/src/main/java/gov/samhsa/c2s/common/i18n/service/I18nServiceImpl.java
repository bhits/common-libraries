package gov.samhsa.c2s.common.i18n.service;

import gov.samhsa.c2s.common.i18n.I18nEnabled;
import gov.samhsa.c2s.common.i18n.domain.I18nMessage;
import gov.samhsa.c2s.common.i18n.domain.I18nMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Slf4j
public class I18nServiceImpl implements I18nService {

    @Autowired
    private I18nMessageRepository i18nMessageRepository;

    @Override
    @Transactional(readOnly = true)
    public String getI18nMessage(I18nEnabled entity, String fieldName, Supplier<String> defaultMessageSupplier) {
        final String locale = LocaleContextHolder.getLocale().getLanguage();
        return i18nMessageRepository
                .findByKeyAndLocale(entity.getMessageKey(fieldName), locale)
                .map(I18nMessage::getMessage)
                .orElseGet(defaultMessageSupplier);
    }
}