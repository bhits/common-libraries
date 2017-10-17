package gov.samhsa.c2s.common.consentgen;

import gov.samhsa.c2s.common.document.transformer.XmlTransformer;
import gov.samhsa.c2s.common.param.ParamsBuilder;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Consent;
import org.hl7.fhir.dstu3.model.Consent.ExceptComponent;
import org.hl7.fhir.dstu3.model.DomainResource;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.ResourceType;
import org.hl7.fhir.r4.model.codesystems.V3ParticipationType;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The Class ConsentBuilderImpl.
 */
public class ConsentBuilderImpl implements ConsentBuilder {

    /**
     * The Constant PARAM_EID.
     */
    public final static String PARAM_EID = "enterpriseIdentifier";

    /**
     * The Constant PARAM_MRN.
     */
    public final static String PARAM_MRN = "medicalRecordNumber";

    /**
     * The Constant PARAM_POLICY_ID.
     */
    public final static String PARAM_POLICY_ID = "policyId";

    /**
     * The Constant PROVIDER_ID_CODE_SYSTEM, which indicates the code system used to express whatever id is used to identify providers
     */
    final static String PROVIDER_ID_CODE_SYSTEM = "http://hl7.org/fhir/sid/us-npi";   // Code system for NPI

    /**
     * The c2s account org.
     */
    private final String c2sAccountOrg;

    /**
     * The xacml xsl url provider.
     */
    private final XacmlXslUrlProvider xacmlXslUrlProvider;

    /**
     * The consent dto factory.
     */
    private final ConsentDtoFactory consentDtoFactory;

    /**
     * The xml transformer.
     */
    private final XmlTransformer xmlTransformer;

    /**
     * Instantiates a new consent builder impl.
     *
     * @param c2sAccountOrg       the c2s account org
     * @param xacmlXslUrlProvider the xacml xsl url provider
     * @param consentDtoFactory   the consent dto factory
     * @param xmlTransformer      the xml transformer
     */
    public ConsentBuilderImpl(String c2sAccountOrg,
                              XacmlXslUrlProvider xacmlXslUrlProvider,
                              ConsentDtoFactory consentDtoFactory, XmlTransformer xmlTransformer) {
        super();
        this.c2sAccountOrg = c2sAccountOrg;
        this.xacmlXslUrlProvider = xacmlXslUrlProvider;
        this.consentDtoFactory = consentDtoFactory;
        this.xmlTransformer = xmlTransformer;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.samhsa.consent.ConsentBuilder#buildConsent2Cdar2(long)
     */
    @Override
    public String buildConsent2Cdar2(long consentId) throws ConsentGenException {
        try {
            final ConsentDto consentDto = consentDtoFactory
                    .createConsentDto(consentId);
            final String cdar2 = xmlTransformer.transform(consentDto,
                    xacmlXslUrlProvider.getUrl(XslResource.CDAR2XSLNAME),
                    Optional.empty(), Optional.empty());
            return cdar2;
        }
        catch (final Exception e) {
            throw new ConsentGenException(e.getMessage(), e);
        }
    }


    @Override
    public String buildConsent2Cdar2ConsentDirective(Object obj) throws ConsentGenException {
        try {
            final ConsentDto consentDto = consentDtoFactory
                    .createConsentDto(obj);
            final String cdar2consentDirective = xmlTransformer.transform(consentDto,
                    xacmlXslUrlProvider.getUrl(XslResource.CDAR2CONSENTDIRECTIVEXSLNAME),
                    Optional.empty(), Optional.empty());
            return cdar2consentDirective;
        }
        catch (final Exception e) {
            throw new ConsentGenException(e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * gov.samhsa.consent.ConsentBuilder#buildConsent2Xacml(java.lang.Object)
     */
    @Override
    public String buildConsent2Xacml(Object obj) throws ConsentGenException {
        try {
            final ConsentDto consentDto = consentDtoFactory
                    .createConsentDto(obj);
            final String xacml = xmlTransformer.transform(consentDto,
                    xacmlXslUrlProvider.getUrl(XslResource.XACMLXSLNAME),
                    Optional.of(ParamsBuilder.withParam(PARAM_MRN, consentDto
                            .getPatientDto().getMedicalRecordNumber())),
                    Optional.empty());
            return xacml;
        }
        catch (final Exception e) {
            throw new ConsentGenException(e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * gov.samhsa.consent.ConsentBuilder#buildFhirConsent2ConsentDto(java.lang.Object)
     */
    @Override
    public ConsentDto buildFhirConsent2ConsentDto(Object objFhirConsent, Object objFhirPatient) throws ConsentGenException {
        try {
            Consent fhirConsent;
            Patient fhirPatient;

            if (objFhirConsent.getClass() == Consent.class) {
                fhirConsent = (Consent) objFhirConsent;
            } else {
                throw new ConsentGenException("Invalid Object type for objFhirConsent passed to 'buildFhirConsent2ConsentDto' method; Object type must be 'org.hl7.fhir.dstu3.model.Consent'");
            }

            if (objFhirPatient.getClass() == Patient.class) {
                fhirPatient = (Patient) objFhirPatient;
            } else {
                throw new ConsentGenException("Invalid Object type for objFhirPatient passed to 'buildFhirConsent2ConsentDto' method; Object type must be 'org.hl7.fhir.dstu3.model.Patient'");
            }

            ConsentDto consentDto = new ConsentDto();
            PatientDto patientDto = new PatientDto();

            // TODO: Search through all Patient identifiers to find the one matching the specific configured code system for MRN
            patientDto.setMedicalRecordNumber(fhirPatient.getIdentifier().get(0).getValue());
            patientDto.setLastName(fhirPatient.getNameFirstRep().getFamily());
            patientDto.setFirstName(fhirPatient.getNameFirstRep().getGivenAsSingleString());

            /* Map FHIR consent fields to ConsentDto fields */

            // Map patient Dto
            consentDto.setPatientDto(patientDto);

            // Map consent reference ID
            consentDto.setConsentReferenceid(fhirConsent.getIdentifier().getValue());

            // Map consent start, end, and signed dates
            consentDto.setConsentStart(fhirConsent.getPeriod().getStart());
            consentDto.setConsentEnd(fhirConsent.getPeriod().getEnd());
            consentDto.setSignedDate(fhirConsent.getDateTime());

            // Map providers permitted to disclose (i.e. "from" providers)
            consentDto = mapProvidersPermittedToDisclose(consentDto, fhirConsent);

            // Map providers to which disclosure is made (i.e. "to" providers)
            consentDto = mapProvidersDisclosureIsMadeTo(consentDto, fhirConsent);

            // Map share for purpose of use codes
            consentDto = mapShareForPurposeOfUseCodes(consentDto, fhirConsent);

            // Map share sensitivity policy codes
            consentDto = mapShareSensitivityPolicyCodes(consentDto, fhirConsent);

            return consentDto;
        }
        catch (final Exception e) {
            throw new ConsentGenException(e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * gov.samhsa.consent.ConsentBuilder#buildConsent2XacmlPdfConsentFrom(java
     * .lang.Object)
     */
    @Override
    public String buildConsent2XacmlPdfConsentFrom(Object obj)
            throws ConsentGenException {
        try {
            final ConsentDto consentDto = consentDtoFactory
                    .createConsentDto(obj);
            final String consentId = consentDto.getConsentReferenceid();
            String policyId = null;
            if (consentId != null) {
                policyId = buildPdfPolicyId(consentId, true);
            }
            final String xacml = xmlTransformer.transform(consentDto,
                    xacmlXslUrlProvider
                            .getUrl(XslResource.XACMLPDFCONSENTFROMXSLNAME),
                    Optional.of(ParamsBuilder
                            .withParam(
                                    PARAM_MRN,
                                    consentDto.getPatientDto()
                                            .getMedicalRecordNumber()).and(
                                    PARAM_POLICY_ID, policyId)), Optional
                            .empty());
            return xacml;
        }
        catch (final Exception e) {
            throw new ConsentGenException(e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * gov.samhsa.consent.ConsentBuilder#buildConsent2XacmlPdfConsentTo(java
     * .lang.Object)
     */
    @Override
    public String buildConsent2XacmlPdfConsentTo(Object obj)
            throws ConsentGenException {
        try {
            final ConsentDto consentDto = consentDtoFactory
                    .createConsentDto(obj);
            final String consentId = consentDto.getConsentReferenceid();
            String policyId = null;
            if (consentId != null) {
                policyId = buildPdfPolicyId(consentId, false);
            }
            final String xacml = xmlTransformer.transform(consentDto,
                    xacmlXslUrlProvider
                            .getUrl(XslResource.XACMLPDFCONSENTTOXSLNAME),
                    Optional.of(ParamsBuilder
                            .withParam(
                                    PARAM_MRN,
                                    consentDto.getPatientDto()
                                            .getMedicalRecordNumber()).and(
                                    PARAM_POLICY_ID, policyId)), Optional
                            .empty());
            return xacml;
        }
        catch (final Exception e) {
            throw new ConsentGenException(e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * gov.samhsa.consent.ConsentBuilder#extractNpiFromFhirProviderResource(org.hl7.fhir.dstu3.model.DomainResource)
     */
    @Override
    public String extractNpiFromFhirProviderResource(DomainResource providerResource) throws ConsentGenException {
        ResourceType providerResourceType = providerResource.getResourceType();
        String providerNpi;

        if (providerResourceType == ResourceType.Organization) {
            Organization providerOrgResource = (Organization) providerResource;
            providerNpi = providerOrgResource.getIdentifier().stream()
                    .filter(i -> (i.hasSystem()) && (i.getSystem().equalsIgnoreCase(PROVIDER_ID_CODE_SYSTEM)))
                    .findFirst()
                    .map(Identifier::getValue)
                    .orElseThrow(() ->
                            new ConsentGenException("Unable to find a provider identifier in the FHIR consent which is under the code system " + PROVIDER_ID_CODE_SYSTEM)
                    );

        } else if (providerResourceType == ResourceType.Practitioner) {
            Practitioner providerIndvResource = (Practitioner) providerResource;
            providerNpi = providerIndvResource.getIdentifier().stream()
                    .filter(i -> (i.hasSystem()) && (i.getSystem().equalsIgnoreCase(PROVIDER_ID_CODE_SYSTEM)))
                    .findFirst()
                    .map(Identifier::getValue)
                    .orElseThrow(() ->
                            new ConsentGenException("Unable to find a provider identifier in the FHIR consent which is under the code system " + PROVIDER_ID_CODE_SYSTEM)
                    );

        } else {
            throw new ConsentGenException("Invalid provider resource type passed to extractNpiFromFhirProviderResource; ResourceType of providerResource must be either 'Organization' or 'Practitioner'");
        }

        return providerNpi;
    }

    /**
     * Builds the pdf policy id.
     *
     * @param consentId     the consent id
     * @param isConsentFrom the is consent from
     * @return the string
     */
    protected String buildPdfPolicyId(String consentId, boolean isConsentFrom) {
        // String id =
        // "C2S.PG-DEV.RmETWp:&amp;2.16.840.1.113883.3.704.100.200.1.1.3.1&amp;ISO:1578821153:1427467752:XM2UoY";
        final String[] tokens = consentId.split(":");
        final int tokenCount = tokens.length;
        // assert the no of elements as 5
        Assert.isTrue(tokenCount == 5,
                "consent reference id should split into 5 sub elements with : delimiter");
        final String consentTo = tokens[tokenCount - 3];
        final String consentFrom = tokens[tokenCount - 2];
        if (isConsentFrom) {
            tokens[tokenCount - 3] = consentFrom;
            tokens[tokenCount - 2] = c2sAccountOrg;

        } else {
            tokens[tokenCount - 3] = consentTo;
            tokens[tokenCount - 2] = c2sAccountOrg;

        }
        String policyId = StringUtils.join(tokens, ":");
        final String[] pTokens = policyId.split("&amp;");
        policyId = StringUtils.join(pTokens, "&");

        return policyId;
    }

    /**
     * Maps the providers permitted to disclose (i.e. the "from" providers) from the
     * FHIR Consent object to the ConsentDto object.
     *
     * @param consentDto  - The ConsentDto object into which the providers should be mapped
     * @param fhirConsent - The FHIR Consent object which contains the providers to be mapped into consentDto
     * @return The ConsentDto object which contains the mapped providers
     * @throws ConsentGenException - Thrown when the ResourceType of providerResource is not 'Organization' or 'Practitioner'
     */
    private ConsentDto mapProvidersPermittedToDisclose(ConsentDto consentDto, Consent fhirConsent) throws ConsentGenException {
        List<Consent.ConsentActorComponent> fhirFromProviderList = new ArrayList<>();

        if(fhirConsent == null || fhirConsent.getActor() == null || fhirConsent.getActor().size() < 2){
            throw new ConsentGenException("The FHIR consent does not have minimum number of Actors specified");
        }
        for (Consent.ConsentActorComponent actor : fhirConsent.getActor()) {
            if (actor.hasRole()) {
                actor.getRole().getCoding().stream().filter(coding -> coding.getCode().equalsIgnoreCase(V3ParticipationType.INF.toCode())).map(coding -> actor).forEach(fhirFromProviderList::add);
            }
        }
        if (fhirFromProviderList == null || fhirFromProviderList.size() < 1) {
            throw new ConsentGenException("The FHIR consent does not have any FROM provider(s) specified");
        }

        consentDto.setProvidersPermittedToDisclose(new HashSet<>());
        consentDto.setOrganizationalProvidersPermittedToDisclose(new HashSet<>());

        Set<OrganizationalProviderDto> organizationalProviderDtoSet = new HashSet<>();
        Set<IndividualProviderDto> individualProviderDtoSet = new HashSet<>();

        for (Consent.ConsentActorComponent fhirFromProviderActor : fhirFromProviderList) {
            DomainResource fhirFromProviderResource = (DomainResource) fhirFromProviderActor.getReference().getResource();
            String fhirFromProviderNpi = extractNpiFromFhirProviderResource(fhirFromProviderResource);

            if (fhirFromProviderResource.getResourceType() == ResourceType.Organization) {
                OrganizationalProviderDto organizationalProviderDto = new OrganizationalProviderDto();
                organizationalProviderDto.setNpi(fhirFromProviderNpi);
                organizationalProviderDtoSet.add(organizationalProviderDto);
            } else if (fhirFromProviderResource.getResourceType() == ResourceType.Practitioner) {
                IndividualProviderDto individualProviderDto = new IndividualProviderDto();
                individualProviderDto.setNpi(fhirFromProviderNpi);
                individualProviderDtoSet.add(individualProviderDto);
            } else {
                throw new ConsentGenException("Invalid from provider resource type found in FHIR consent; ResourceType of fhirFromProviderResource must be either 'Organization' or 'Practitioner'");
            }
        }
        consentDto.setOrganizationalProvidersPermittedToDisclose(organizationalProviderDtoSet);
        consentDto.setProvidersPermittedToDisclose(individualProviderDtoSet);

        return consentDto;
    }

    /**
     * Maps the providers disclosure is made to (i.e. the "to" providers) from the
     * FHIR Consent object to the ConsentDto object.
     *
     * @param consentDto  - The ConsentDto object into which the providers should be mapped
     * @param fhirConsent - The FHIR Consent object which contains the providers to be mapped into consentDto
     * @return The ConsentDto object which contains the mapped providers
     * @throws ConsentGenException - Thrown when the ResourceType of providerResource is not 'Organization' or 'Practitioner'
     */
    private ConsentDto mapProvidersDisclosureIsMadeTo(ConsentDto consentDto, Consent fhirConsent) throws ConsentGenException {
        List<Consent.ConsentActorComponent> fhirToProviderList = new ArrayList<>();

        if(fhirConsent == null || fhirConsent.getActor() == null || fhirConsent.getActor().size() < 2){
            throw new ConsentGenException("The FHIR consent does not have minimum number of Actors specified");
        }

        for (Consent.ConsentActorComponent actor : fhirConsent.getActor()) {
            if (actor.hasRole()) {
                actor.getRole().getCoding().stream().filter(coding -> coding.getCode().equalsIgnoreCase(V3ParticipationType.IRCP.toCode())).map(coding -> actor).forEach(fhirToProviderList::add);
            }
        }

        if (fhirToProviderList == null || fhirToProviderList.size() < 1) {
            throw new ConsentGenException("The FHIR consent does not have any TO provider(s) specified");
        }

        consentDto.setProvidersDisclosureIsMadeTo(new HashSet<>());
        consentDto.setOrganizationalProvidersDisclosureIsMadeTo(new HashSet<>());

        Set<OrganizationalProviderDto> organizationalProviderDtoSet = new HashSet<>();
        Set<IndividualProviderDto> individualProviderDtoSet = new HashSet<>();

        for (Consent.ConsentActorComponent fhirToProviderActor : fhirToProviderList) {
            DomainResource fhirToProviderResource = (DomainResource) fhirToProviderActor.getReference().getResource();
            String fhirFromProviderNpi = extractNpiFromFhirProviderResource(fhirToProviderResource);

            if (fhirToProviderResource.getResourceType() == ResourceType.Organization) {
                OrganizationalProviderDto organizationalProviderDto = new OrganizationalProviderDto();
                organizationalProviderDto.setNpi(fhirFromProviderNpi);
                organizationalProviderDtoSet.add(organizationalProviderDto);
            } else if (fhirToProviderResource.getResourceType() == ResourceType.Practitioner) {
                IndividualProviderDto individualProviderDto = new IndividualProviderDto();
                individualProviderDto.setNpi(fhirFromProviderNpi);
                individualProviderDtoSet.add(individualProviderDto);
            } else {
                throw new ConsentGenException("Invalid to provider resource(s) type found in FHIR consent; ResourceType of fhirToProviderResource must be either 'Organization' or 'Practitioner'");
            }
        }

        consentDto.setOrganizationalProvidersDisclosureIsMadeTo(organizationalProviderDtoSet);
        consentDto.setProvidersDisclosureIsMadeTo(individualProviderDtoSet);

        return consentDto;
    }

    /**
     * Maps the share for purpose of use codes from the FHIR Consent object to the ConsentDto object.
     *
     * @param consentDto  - The ConsentDto object into which the share for purpose of use codes should be mapped
     * @param fhirConsent - The FHIR Consent object which contains the share for purpose of use codes to be mapped into consentDto
     * @return The ConsentDto object which contains the mapped share for purpose of use codes
     * @throws ConsentGenException - Thrown when FHIR consent contains no 'purpose' codes, or when extracted purpose of use codes set is empty
     */
    private ConsentDto mapShareForPurposeOfUseCodes(ConsentDto consentDto, Consent fhirConsent) throws ConsentGenException {
        Set<Coding> fhirShareForPurposeOfUseCodes;
        Set<TypeCodesDto> consentDtoShareForPurposeOfUseCodes = new HashSet<>();

        if (fhirConsent.hasPurpose()) {
            fhirShareForPurposeOfUseCodes = new HashSet<>(fhirConsent.getPurpose());
        } else {
            throw new ConsentGenException("FHIR consent does not contain any 'purpose' codes");
        }

        if (fhirShareForPurposeOfUseCodes.size() > 0) {
            fhirShareForPurposeOfUseCodes.forEach(pou -> {
                TypeCodesDto pouCodeDto = new TypeCodesDto();

                pouCodeDto.setCodeSystem(pou.getSystem());
                pouCodeDto.setCode(pou.getCode());

                if (pou.hasDisplay()) {
                    pouCodeDto.setDisplayName(pou.getDisplay());
                }

                consentDtoShareForPurposeOfUseCodes.add(pouCodeDto);
            });
        } else {
            throw new ConsentGenException("Share for purpose of use codes set extracted from FHIR consent is an empty set");
        }

        consentDto.setShareForPurposeOfUseCodes(consentDtoShareForPurposeOfUseCodes);

        return consentDto;
    }

    /**
     * Maps the share sensitivity policy codes from the FHIR Consent object to the ConsentDto object.
     *
     * @param consentDto  - The ConsentDto object into which the sensitivity policy codes should be mapped
     * @param fhirConsent - The FHIR Consent object which contains the sensitivity policy codes to be mapped into consentDto
     * @return The ConsentDto object which contains the mapped sensitivity policy codes
     * @throws ConsentGenException - Thrown when FHIR consent contains no 'except' anr/or 'securityLabel' codes, when the codes in the FHIR
     *                             consent are of a type other than 'permit', or when the extracted sensitivity policy codes set size is != 1
     */
    private ConsentDto mapShareSensitivityPolicyCodes(ConsentDto consentDto, Consent fhirConsent) throws ConsentGenException {
        List<ExceptComponent> fhirExceptComponentsList;
        Set<Coding> fhirShareSensitivityPolicyCodes;
        Set<TypeCodesDto> consentDtoShareSensitivityPolicyCodes = new HashSet<>();

        if (fhirConsent.hasExcept()) {
            fhirExceptComponentsList = fhirConsent.getExcept();
        } else {
            throw new ConsentGenException("FHIR consent does not contain any 'except' codes");
        }

        List<ExceptComponent> filteredFhirExceptComponentsList = fhirExceptComponentsList.stream()
                .filter(ExceptComponent::hasType)
                .filter(exceptComponent -> exceptComponent.getType() == Consent.ConsentExceptType.PERMIT)
                .collect(Collectors.toList());

        ExceptComponent fhirPermitTypeExceptComponenet;

        if (filteredFhirExceptComponentsList.size() == 1) {
            fhirPermitTypeExceptComponenet = filteredFhirExceptComponentsList.get(0);
        } else {
            throw new ConsentGenException("FHIR consent 'except' list contains no except components with type='permit', or it contains more than 1");
        }

        if (fhirPermitTypeExceptComponenet.hasSecurityLabel()) {
            fhirShareSensitivityPolicyCodes = new HashSet<>(fhirPermitTypeExceptComponenet.getSecurityLabel());
        } else {
            throw new ConsentGenException("FHIR consent 'except' component with type='permit' does not contain any 'securityLabel' codes");
        }

        if (fhirShareSensitivityPolicyCodes.size() > 0) {
            fhirShareSensitivityPolicyCodes.forEach(sensitivityPolicyCode -> {
                TypeCodesDto sensitivityPolicyCodeDto = new TypeCodesDto();

                sensitivityPolicyCodeDto.setCodeSystem(sensitivityPolicyCode.getSystem());
                sensitivityPolicyCodeDto.setCode(sensitivityPolicyCode.getCode());

                if (sensitivityPolicyCode.hasDisplay()) {
                    sensitivityPolicyCodeDto.setDisplayName(sensitivityPolicyCode.getDisplay());
                }

                consentDtoShareSensitivityPolicyCodes.add(sensitivityPolicyCodeDto);
            });
        } else {
            throw new ConsentGenException("Sensitivity policy codes set extracted from FHIR consent is an empty set");
        }

        consentDto.setShareSensitivityPolicyCodes(consentDtoShareSensitivityPolicyCodes);

        return consentDto;
    }
}
