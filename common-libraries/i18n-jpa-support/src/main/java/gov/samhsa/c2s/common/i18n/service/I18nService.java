package gov.samhsa.c2s.common.i18n.service;

import gov.samhsa.c2s.common.i18n.I18nEnabled;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

public interface I18nService {

    @Transactional(readOnly = true)
    String getI18nMessage(I18nEnabled entityReference, String fieldName, Supplier<String> defaultMessageSupplier);
}
