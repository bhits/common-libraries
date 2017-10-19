package gov.samhsa.c2s.common.consentgen;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class IndividualProviderExportDto.
 */
@XmlRootElement
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IndividualProviderDto {

    /**
     * The last name.
     */
    @NotNull
    @Size(max = 30)
    private String lastName;

    /**
     * The first name.
     */
    @NotNull
    @Size(max = 30)
    private String firstName;

    /**
     * The middle name.
     */
    @NotNull
    @Size(max = 30)
    private String middleName;

    /**
     * The name prefix.
     */
    @NotNull
    @Size(max = 30)
    private String namePrefix;

    /**
     * The name suffix.
     */
    @NotNull
    @Size(max = 30)
    private String nameSuffix;

    /**
     * The npi.
     */
    @NotNull
    @Size(max = 30)
    private String npi;

    /**
     * The enumeration date.
     */
    @NotNull
    @Size(max = 30)
    private String enumerationDate;

    /**
     * The practice location address telephone number.
     */
    @NotNull
    @Size(max = 30)
    private String practiceLocationAddressTelephoneNumber;

    /**
     * The first line practice location address.
     */
    @NotNull
    @Size(max = 30)
    private String firstLinePracticeLocationAddress;

    /**
     * The second line practice location address.
     */
    @NotNull
    @Size(max = 30)
    private String secondLinePracticeLocationAddress;

    /**
     * The practice location address city name.
     */
    @NotNull
    @Size(max = 30)
    private String practiceLocationAddressCityName;

    /**
     * The practice location address state name.
     */
    @NotNull
    @Size(max = 30)
    private String practiceLocationAddressStateName;

    /**
     * The practice location address postal code.
     */
    @NotNull
    @Size(max = 30)
    private String practiceLocationAddressPostalCode;

    /**
     * The practice location address country code.
     */
    @NotNull
    @Size(max = 30)
    private String practiceLocationAddressCountryCode;
}
