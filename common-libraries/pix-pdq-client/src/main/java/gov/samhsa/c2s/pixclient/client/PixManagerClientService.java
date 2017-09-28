package gov.samhsa.c2s.pixclient.client;

/**
 * The Interface PixManagerClientService.
 */
public interface PixManagerClientService {

    /**
     * Adds the patient.
     *
     * @param reqXMLPath the req xml path
     * @return the string
     */
    String addPatient(String reqXMLPath);

    /**
     * Update patient.
     *
     * @param reqXMLPath the req xml path
     * @return the string
     */
    String updatePatient(String reqXMLPath);

    /**
     * Query Patient for Enterprise Id
     * @param patientId The patient Id
     * @param PatientMrnOid The MRN System OID
     * @return EnterpriseId comprising of Identifier, Universal Identifier and Universal Identifier Type
     */
    String queryForEnterpriseId(String patientId, String PatientMrnOid);

}
