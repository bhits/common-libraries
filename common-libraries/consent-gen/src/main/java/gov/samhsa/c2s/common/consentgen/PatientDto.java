package gov.samhsa.c2s.common.consentgen;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * The Class PatientExportDto.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientDto {

    /**
     * The first name.
     */
    private String firstName;

    /**
     * The last name.
     */
    private String lastName;

    /**
     * The prefix.
     */
    @Size(max = 30)
    private String prefix;

    /**
     * The address street address line.
     */
    private String addressStreetAddressLine;

    /**
     * The address city.
     */
    private String addressCity;

    /**
     * The address state code.
     */
    private String addressStateCode;

    /**
     * The address postal code.
     */
    @Pattern(regexp = "(^\\d{5}$|^\\d{5}-\\d{4})*")
    private String addressPostalCode;

    /**
     * The address country code.
     */
    private String addressCountryCode;

    /**
     * The birth date.
     */
    @Past
    @DateTimeFormat(pattern = "yyyyMMdd")
    @XmlJavaTypeAdapter(XMLIntegerDateAdapter.class)
    private Date birthDate;
    /**
     * The administrative gender code.
     */
    private String administrativeGenderCode;
    /**
     * The telephone type telephone.
     */
    @Pattern(regexp = "(^\\(?([0-9]{3})\\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4}))*")
    private String telephoneTypeTelephone;
    /**
     * The email.
     */
    private String email;
    /**
     * The social security number.
     */
    @Pattern(regexp = "(\\d{3}-?\\d{2}-?\\d{4})*")
    private String socialSecurityNumber;
    /**
     * The medical record number.
     */
    @Size(max = 30)
    private String medicalRecordNumber;
    /**
     * The enterprise identifier.
     */
    @Size(max = 255)
    private String enterpriseIdentifier;
    /**
     * The patient id number.
     */
    @Size(max = 30)
    private String patientIdNumber;
}
