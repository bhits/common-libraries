package gov.samhsa.c2s.common.consentgen;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class MockConsentDtoFactory implements ConsentDtoFactory {

    public static final Object MOCK_REQUEST_OBJECT = new Object();
    public static final String MOCK_CONSENT_REFERENCE_ID = "consentReferenceId";
    public static final String MOCK_FIRST_NAME = "firstName";
    public static final String MOCK_LAST_NAME = "lastName";
    public static final String MOCK_MRN = "mrn";
    public static final String INTERMEDIARY_NPI_1 = "1111111111";
    public static final String INTERMEDIARY_NPI_2 = "2222222222";
    public static final String INTERMEDIARY_NPI_3 = "3333333333";
    public static final Set<String> INTERMEDIARY_NPIS = Collections.unmodifiableSet(Stream.of(INTERMEDIARY_NPI_1, INTERMEDIARY_NPI_2, INTERMEDIARY_NPI_3).collect(toSet()));
    public static final String RECIPIENT_NPI_1 = "4444444444";
    public static final String RECIPIENT_NPI_2 = "5555555555";
    public static final String RECIPIENT_NPI_3 = "6666666666";
    public static final Set<String> RECIPIENT_NPIS = Collections.unmodifiableSet(Stream.of(RECIPIENT_NPI_1, RECIPIENT_NPI_2, RECIPIENT_NPI_3).collect(toSet()));
    public static final String PURPOSE_TREAT = "TREAT";
    public static final String PURPOSE_HPAYMT = "HPAYMT";
    public static final String PURPOSE_HRESCH = "HRESCH";
    public static final Set<String> PURPOSES = Collections.unmodifiableSet(Stream.of(PURPOSE_TREAT, PURPOSE_HPAYMT, PURPOSE_HRESCH).collect(toSet()));
    public static final String ALC = "ALC";
    public static final String COM = "COM";
    public static final String PSY = "PSY";
    public static final String HIV = "HIV";
    public static final String ETH = "ETH";
    public static final String SEX = "SEX";
    public static final Set<String> OBLIGATIONS = Collections.unmodifiableSet(Stream.of(ALC, COM, PSY, HIV, ETH, SEX).collect(toSet()));

    @Override
    public ConsentDto createConsentDto(long consentId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConsentDto createConsentDto(Object obj) {
        if (MOCK_REQUEST_OBJECT.equals(obj)) {
            final Instant now = Instant.now();
            final ConsentDto consentDto = ConsentDto.builder()
                    .consentReferenceid(MOCK_CONSENT_REFERENCE_ID)
                    .patientDto(PatientDto.builder().firstName(MOCK_FIRST_NAME).lastName(MOCK_LAST_NAME).medicalRecordNumber(MOCK_MRN).build())
                    // intermediary
                    .providersPermittedToDisclose(individuals(individual(INTERMEDIARY_NPI_1), individual(INTERMEDIARY_NPI_3)))
                    .organizationalProvidersPermittedToDisclose(organizations(organization(INTERMEDIARY_NPI_2)))
                    // recipient
                    .providersDisclosureIsMadeTo(individuals(individual(RECIPIENT_NPI_2)))
                    .organizationalProvidersDisclosureIsMadeTo(organizations(organization(RECIPIENT_NPI_1), organization(RECIPIENT_NPI_3)))
                    .consentStart(Date.from(now))
                    .consentEnd(Date.from(now.plus(365, ChronoUnit.DAYS)))
                    .shareSensitivityPolicyCodes(sensitivities(
                            sensitivity(ALC, "Alcohol use and Alcoholism Information"),
                            sensitivity(COM, "Communicable disease information"),
                            sensitivity(PSY, "Mental health information"),
                            sensitivity(HIV, "HIV/AIDS information"),
                            sensitivity(ETH, "Drug use information"),
                            sensitivity(SEX, "Sexuality and reproductive health information")))
                    .shareForPurposeOfUseCodes(purposes(
                            purpose(PURPOSE_TREAT, "Treatment"),
                            purpose(PURPOSE_HPAYMT, "Healthcare Payment"),
                            purpose(PURPOSE_HRESCH, "Healthcare Research")
                    ))
                    .build();
            return consentDto;
        } else {
            throw new RuntimeException("Invalid request object");
        }
    }

    private Set<TypeCodesDto> sensitivities(TypeCodesDto... sensitivities) {
        return Arrays.stream(sensitivities).collect(toSet());
    }

    private TypeCodesDto sensitivity(String code, String displayName) {
        return TypeCodesDto.builder()
                .code(code)
                .displayName(displayName)
                .codeSystem("http://hl7.org/fhir/v3/ActReason")
                .build();
    }

    private Set<TypeCodesDto> purposes(TypeCodesDto... purposes) {
        return Arrays.stream(purposes).collect(toSet());
    }

    private TypeCodesDto purpose(String code, String displayName) {
        return TypeCodesDto.builder()
                .code(code)
                .displayName(displayName)
                .codeSystem("http://hl7.org/fhir/v3/ActReason")
                .build();
    }

    private Set<IndividualProviderDto> individuals(IndividualProviderDto... providers) {
        return Arrays.stream(providers).collect(toSet());
    }

    private IndividualProviderDto individual(String npi) {
        return IndividualProviderDto.builder()
                .npi(npi)
                .build();
    }

    private Set<OrganizationalProviderDto> organizations(OrganizationalProviderDto... organizations) {
        return Arrays.stream(organizations).collect(toSet());
    }

    private OrganizationalProviderDto organization(String npi) {
        return OrganizationalProviderDto.builder()
                .npi(npi)
                .build();
    }
}