package gov.samhsa.c2s.common.consentgen;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.Set;


@XmlRootElement(name = "ConsentExport")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsentDto {

    /**
     * The consent referenceid.
     */
    @XmlElement(name = "id")
    private String consentReferenceid;

    /**
     * The patient export dto.
     */
    @XmlElement(name = "Patient")
    private PatientDto patientDto;

    /**
     * The providers permitted to disclose.
     */
    @XmlElementWrapper(name = "IndividualProvidersPermittedToDiscloseList")
    @XmlElement(name = "IndividualProvidersPermittedToDisclose")
    private Set<IndividualProviderDto> providersPermittedToDisclose;

    /**
     * The providers disclosure is made to.
     */
    @XmlElementWrapper(name = "IndividualProvidersDisclosureIsMadeToList")
    @XmlElement(name = "IndividualProvidersDisclosureIsMadeTo")
    private Set<IndividualProviderDto> providersDisclosureIsMadeTo;

    /**
     * The organizational providers permitted to disclose.
     */
    @XmlElementWrapper(name = "OrganizationalProvidersPermittedToDiscloseList")
    @XmlElement(name = "OrganizationalProvidersPermittedToDisclose")
    private Set<OrganizationalProviderDto> organizationalProvidersPermittedToDisclose;

    /**
     * The organizational providers disclosure is made to.
     */
    @XmlElementWrapper(name = "OrganizationalProvidersDisclosureIsMadeToList")
    @XmlElement(name = "OrganizationalProvidersDisclosureIsMadeTo")
    private Set<OrganizationalProviderDto> organizationalProvidersDisclosureIsMadeTo;

    /**
     * The consent start.
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    @XmlJavaTypeAdapter(XMLDateAdapter.class)
    private Date consentStart;

    /**
     * The consent end.
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    @XmlJavaTypeAdapter(XMLDateAdapter.class)
    private Date consentEnd;

    /**
     * The signed date.
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    @XmlJavaTypeAdapter(XMLDateAdapter.class)
    private Date signedDate;

    /**
     * The revocation date.
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    @XmlJavaTypeAdapter(XMLDateAdapter.class)
    private Date revocationDate;

    /**
     * The version.
     */
    private Integer version;

    /**
     * The legal representative.
     */
    @ManyToOne
    private PatientDto legalRepresentative;

    /**
     * The do not share clinical document type codes.
     */
    @XmlElementWrapper(name = "doNotShareClinicalDocumentTypeCodesList")
    private Set<TypeCodesDto> doNotShareClinicalDocumentTypeCodes;

    /**
     * The do not share clinical document section type codes.
     */
    @XmlElementWrapper(name = "doNotShareClinicalDocumentSectionTypeCodesList")
    private Set<TypeCodesDto> doNotShareClinicalDocumentSectionTypeCodes;

    /**
     * The do not share sensitivity policy codes.
     */
    @XmlElementWrapper(name = "doNotShareSensitivityPolicyCodesList")
    private Set<TypeCodesDto> doNotShareSensitivityPolicyCodes;

    /**
     * The share sensitivity policy codes.
     */
    @XmlElementWrapper(name = "shareSensitivityPolicyCodesList")
    private Set<TypeCodesDto> shareSensitivityPolicyCodes;

    /**
     * The do not share for purpose of use codes.
     */
    @XmlElementWrapper(name = "shareForPurposeOfUseCodesList")
    private Set<TypeCodesDto> shareForPurposeOfUseCodes;

    /**
     * The do not share clinical concept codes.
     */
    @XmlElementWrapper(name = "doNotShareClinicalConceptCodesList")
    private Set<TypeCodesDto> doNotShareClinicalConceptCodes;
}
