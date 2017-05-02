package gov.samhsa.c2s.common.consentgen;

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
     * @param obj
     *            the obj
     * @return the ConsentDto
     * @throws ConsentGenException
     *             the consent gen exception
     */
    ConsentDto buildFhirConsent2ConsentDto(Object obj) throws ConsentGenException;

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
}
