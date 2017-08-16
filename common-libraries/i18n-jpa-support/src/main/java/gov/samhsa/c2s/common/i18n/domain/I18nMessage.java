package gov.samhsa.c2s.common.i18n.domain;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class I18nMessage {
    @Id
    @GeneratedValue
    private long id;

    @NotBlank
    private String key;

    @NotBlank
    private String description;

    @NotBlank
    @Column(length = 20000)
    private String message;

    @NotBlank
    private String locale;
}