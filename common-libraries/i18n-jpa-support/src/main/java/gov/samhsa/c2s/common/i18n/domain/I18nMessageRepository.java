package gov.samhsa.c2s.common.i18n.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface I18nMessageRepository extends JpaRepository<I18nMessage, Long> {

    Optional<I18nMessage> findByKeyAndLocale(String key, String locale);
}
