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
    public String addPerson(String reqXMLPath);

    /**
     * Update person.
     *
     * @param reqXMLPath the req xml path
     * @return the string
     */
    public String updatePerson(String reqXMLPath);

    /**
     * Query person.
     *
     * @param xmlFilePath the xml file path
     * @return the pix manager bean
     */
    public PixManagerBean queryPerson(String xmlFilePath);

}
