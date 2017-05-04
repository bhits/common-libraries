package gov.samhsa.c2s.common.consentgen;

import org.hl7.fhir.dstu3.model.DomainResource;

/**
 * The Interface ConsentBuilder.
 */
public interface ConsentBuilder {

    /**
     * Builds the consent2 cdar2.
     *
     * @param consentId
     *            the consent id
     * @return the string
     * @throws ConsentGenException
     *             the consent gen exception
     */
    String buildConsent2Cdar2(long consentId) throws ConsentGenException;


    String buildConsent2Cdar2ConsentDirective(Object obj) throws ConsentGenException;

    /**
     * Builds the consent2 xacml.
     *
     * @param obj
     *            the obj
     * @return the string
     * @throws ConsentGenException
     *             the consent gen exception
     */
    String buildConsent2Xacml(Object obj) throws ConsentGenException;

    /**
     * Builds the FHIR Consent to ConsentDto.
     *
     * @param objFhirConsent
     *            the FHIR Consent object
     * @param objFhirPatient
     *            the FHIR Patient who is the subject of the FHIR Consent
     * @return the ConsentDto
     * @throws ConsentGenException
     *             the consent gen exception
     */
    ConsentDto buildFhirConsent2ConsentDto(Object objFhirConsent, Object objFhirPatient) throws ConsentGenException;

    /**
     * Builds the consent2 xacml for consentFrom provider. to give access to
     * consent pdf from C2S Health
     *
     * @param obj
     *            the obj
     * @return the string
     * @throws ConsentGenException
     *             the consent gen exception
     */
    String buildConsent2XacmlPdfConsentFrom(Object obj)
            throws ConsentGenException;

    /**
     * Builds the consent2 xacml for consentTo provider. to give access to
     * consent pdf from C2S Health
     *
     * @param obj
     *            the obj
     * @return the string
     * @throws ConsentGenException
     *             the consent gen exception
     */
    String buildConsent2XacmlPdfConsentTo(Object obj)
            throws ConsentGenException;

    /**
     * Extract the NPI from a FHIR provider resource object
     *
     * @param providerResource - An 'Organization' or 'Practitioner' object from which to extract the provider NPI
     * @return a string representation of the extracted provider NPI
     * @throws ConsentGenException - Thrown when the ResourceType of providerResource is not 'Organization' or 'Practitioner'
     */
    String extractNpiFromFhirProviderResource(DomainResource providerResource) throws ConsentGenException;
}
