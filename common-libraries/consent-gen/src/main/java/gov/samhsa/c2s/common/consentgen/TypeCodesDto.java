package gov.samhsa.c2s.common.consentgen;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class TypeCodesDto.
 */
@XmlRootElement
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TypeCodesDto {

    private String displayName;
    private String code;
    private String codeSystem;
    private String codeSystemName;
}