package gov.samhsa.c2s.pixclient.client;

import gov.samhsa.c2s.pixclient.util.PixManagerBean;

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
     * Query person.
     *
     * @param xmlFilePath the xml file path
     * @return the pix manager bean
     */
    PixManagerBean queryPerson(String xmlFilePath);

    /**
     *
     * @param patientId The patient Id
     * @param PatientMrnOid The MRN System OID
     * @return EnterpriseId
     */
    String queryForEnterpriseId(String patientId, String PatientMrnOid);

}
