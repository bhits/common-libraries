package gov.samhsa.c2s.pixclient.client;

/**
 * The Interface PixManagerClientService.
 */
public interface PixManagerClientService {

    /**
     * Adds the person.
     *
     * @param reqXMLPath the req xml path
     * @return the string
     */
    String addPerson(String reqXMLPath);

    /**
     * Update person.
     *
     * @param reqXMLPath the req xml path
     * @return the string
     */
    String updatePerson(String reqXMLPath);

    /**
     * Query Person for Enterprise Id
     * @param patientId The patient Id
     * @param PatientMrnOid The MRN System OID
     * @return EnterpriseId comprising of Identifier, Universal Identifier and Universal Identifier Type
     */
    String queryForEnterpriseId(String patientId, String PatientMrnOid);

}
