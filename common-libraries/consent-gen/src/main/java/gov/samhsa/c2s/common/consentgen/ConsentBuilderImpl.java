package gov.samhsa.c2s.common.consentgen;

import gov.samhsa.c2s.common.document.transformer.XmlTransformer;
import gov.samhsa.c2s.common.param.ParamsBuilder;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.model.DomainResource;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.ResourceType;
import org.springframework.util.Assert;

import org.hl7.fhir.dstu3.model.Consent;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Practitioner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * The Class ConsentBuilderImpl.
 */
public class ConsentBuilderImpl implements ConsentBuilder {

    /** The Constant PARAM_EID. */
    public final static String PARAM_EID = "enterpriseIdentifier";

    /** The Constant PARAM_MRN. */
    public final static String PARAM_MRN = "medicalRecordNumber";

    /** The Constant PARAM_POLICY_ID. */
    public final static String PARAM_POLICY_ID = "policyId";

    /** The Constant PROVIDER_ID_CODE_SYSTEM, which indicates the code system used to express whatever id is used to identify providers */
    final static String PROVIDER_ID_CODE_SYSTEM = "http://hl7.org/fhir/sid/us-npi";   // Code system for NPI

    /** The c2s account org. */
    private final String c2sAccountOrg;

    /** The xacml xsl url provider. */
    private final XacmlXslUrlProvider xacmlXslUrlProvider;

    /** The consent dto factory. */
    private final ConsentDtoFactory consentDtoFactory;

    /** The xml transformer. */
    private final XmlTransformer xmlTransformer;

    /**
     * Instantiates a new consent builder impl.
     *
     * @param c2sAccountOrg
     *            the c2s account org
     * @param xacmlXslUrlProvider
     *            the xacml xsl url provider
     * @param consentDtoFactory
     *            the consent dto factory
     * @param xmlTransformer
     *            the xml transformer
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
        } catch (final Exception e) {
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
        } catch (final Exception e) {
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
        } catch (final Exception e) {
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
    public ConsentDto buildFhirConsent2ConsentDto(Object obj) throws ConsentGenException {
        try {
            Consent fhirConsent;

            if(obj.getClass() == Consent.class){
                fhirConsent = (Consent) obj;
            }else{
                throw new ConsentGenException("Invalid Object type passed to 'buildFhirConsent2ConsentDto' method; Object type must be 'org.hl7.fhir.dstu3.model.Consent'");
            }

            ConsentDto consentDto = new ConsentDto();

            // Map FHIR consent fields to ConsentDto fields
            consentDto.setConsentStart(fhirConsent.getPeriod().getStart());
            consentDto.setConsentEnd(fhirConsent.getPeriod().getEnd());
            consentDto.setConsentReferenceid(fhirConsent.getIdentifier().getValue());
            consentDto.setSignedDate(fhirConsent.getDateTime());

            DomainResource fhirFromProviderResource = (DomainResource) fhirConsent.getOrganization().getResource();

            String fhirFromProviderNpi = extractNpiFromProviderResource(fhirFromProviderResource);

            consentDto.setProvidersPermittedToDisclose(new HashSet<>());
            consentDto.setOrganizationalProvidersPermittedToDisclose(new HashSet<>());

            if(fhirFromProviderResource.getResourceType() == ResourceType.Organization){
                OrganizationalProviderDto organizationalProviderDto = new OrganizationalProviderDto();
                organizationalProviderDto.setNpi(fhirFromProviderNpi);

                Set<OrganizationalProviderDto> organizationalProviderDtoSet = new HashSet<>();
                organizationalProviderDtoSet.add(organizationalProviderDto);

                consentDto.setOrganizationalProvidersPermittedToDisclose(organizationalProviderDtoSet);
            }else if(fhirFromProviderResource.getResourceType() == ResourceType.Practitioner){
                IndividualProviderDto individualProviderDto = new IndividualProviderDto();
                individualProviderDto.setNpi(fhirFromProviderNpi);

                Set<IndividualProviderDto> individualProviderDtoSet = new HashSet<>();
                individualProviderDtoSet.add(individualProviderDto);

                consentDto.setProvidersPermittedToDisclose(individualProviderDtoSet);
            }else{
                throw new ConsentGenException("Invalid from provider resource type found in FHIR consent; ResourceType of fhirFromProviderResource must be either 'Organization' or 'Practitioner'");
            }

            return consentDto;
        } catch (final Exception e) {
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
        } catch (final Exception e) {
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
        } catch (final Exception e) {
            throw new ConsentGenException(e.getMessage(), e);
        }
    }

    /**
     * Builds the pdf policy id.
     *
     * @param consentId
     *            the consent id
     * @param isConsentFrom
     *            the is consent from
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
     * Extract the NPI from a provider resource object
     *
     * @param providerResource - An 'Organization' or 'Practitioner' object from which to extract the provider NPI
     * @return a string representation of the extracted provider NPI
     * @throws ConsentGenException - Thrown when the ResourceType of providerResource is not 'Organization' or 'Practitioner'
     */
    private String extractNpiFromProviderResource(DomainResource providerResource) throws ConsentGenException{
        ResourceType providerResourceType = providerResource.getResourceType();
        String providerNpi;

        if(providerResourceType == ResourceType.Organization){
            Organization providerOrgResource = (Organization) providerResource;
            providerNpi = providerOrgResource.getIdentifier().stream()
                    .filter(i -> (i.hasSystem()) && (i.getSystem().equalsIgnoreCase(PROVIDER_ID_CODE_SYSTEM)))
                    .findFirst()
                    .map(Identifier::getValue)
                    .orElseThrow(() ->
                            new ConsentGenException("Unable to find a provider identifier in the FHIR consent which is under the code system " + PROVIDER_ID_CODE_SYSTEM)
                    );

        }else if(providerResourceType == ResourceType.Practitioner){
            Practitioner providerIndvResource = (Practitioner) providerResource;
            providerNpi = providerIndvResource.getIdentifier().stream()
                    .filter(i -> (i.hasSystem()) && (i.getSystem().equalsIgnoreCase(PROVIDER_ID_CODE_SYSTEM)))
                    .findFirst()
                    .map(Identifier::getValue)
                    .orElseThrow(() ->
                            new ConsentGenException("Unable to find a provider identifier in the FHIR consent which is under the code system " + PROVIDER_ID_CODE_SYSTEM)
                    );

        }else{
            throw new ConsentGenException("Invalid provider resource type passed to extractNpiFromProviderResource; ResourceType of providerResource must be either 'Organization' or 'Practitioner'");
        }

        return providerNpi;
    }
}
