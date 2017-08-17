
/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package gov.samhsa.acs.xdsb.repository.wsclient;

import java.util.logging.Logger;

import ihe.iti.xds_b._2007.DocumentRepositoryPortType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * This class was generated by Apache CXF 3.1.12
 * 2017-08-17T13:06:18.774-04:00
 * Generated source version: 3.1.12
 */

@javax.jws.WebService(
                      serviceName = "DocumentRepository_Service",
                      portName = "DocumentRepository_Port_Soap12",
                      targetNamespace = "urn:ihe:iti:xds-b:2007",
                      wsdlLocation = "file:./src/main/resources/wsdl/XDS.b_DocumentRepository.wsdl",
                      endpointInterface = "ihe.iti.xds_b._2007.DocumentRepositoryPortType")
                      
public class DocumentRepository_Port_Soap12Impl implements DocumentRepositoryPortType {

    private static final Logger LOG = Logger.getLogger(DocumentRepository_Port_Soap12Impl.class.getName());

    /* (non-Javadoc)
     * @see ihe.iti.xds_b._2007.DocumentRepositoryPortType#documentRepositoryProvideAndRegisterDocumentSetB(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body)*
     */
    public RegistryResponseType documentRepositoryProvideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType body) { 
        LOG.info("Executing operation documentRepositoryProvideAndRegisterDocumentSetB");
        System.out.println(body);
        try {
            RegistryResponseType _return = new RegistryResponseType();
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    /* (non-Javadoc)
     * @see ihe.iti.xds_b._2007.DocumentRepositoryPortType#documentRepositoryRetrieveDocumentSet(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body)*
     */
    public RetrieveDocumentSetResponseType documentRepositoryRetrieveDocumentSet(RetrieveDocumentSetRequestType body) { 
        LOG.info("Executing operation documentRepositoryRetrieveDocumentSet");
        System.out.println(body);
        try {
            RetrieveDocumentSetResponseType _return = new RetrieveDocumentSetResponseType();
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

}
