package gov.samhsa.c2s.common.xdsbclient.repository.wsclient;

import gov.samhsa.c2s.common.cxf.AbstractEnhancedCxfClient;
import gov.samhsa.c2s.common.log.Logger;
import gov.samhsa.c2s.common.log.LoggerFactory;
import gov.samhsa.c2s.common.xdsbclient.exception.DS4PException;
import ihe.iti.xds_b._2007.DocumentRepositoryService.DocumentRepositoryPortTypeProxy;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import javax.xml.namespace.QName;
import java.net.URL;

public class XdsbRepositoryWebServiceClient extends AbstractEnhancedCxfClient {

    private static final QName SERVICE_NAME = new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Service");
    private final URL wsdlURL = this.getClass().getClassLoader().getResource("wsdl/XDS.b_DocumentRepository.wsdl");
    private final Logger logger = LoggerFactory.getLogger(XdsbRepositoryWebServiceClient.class);

    public XdsbRepositoryWebServiceClient(String endpointAddress) {
        super(SERVICE_NAME, endpointAddress);
        setMtomEnabled(Boolean.TRUE);
    }

    @Override
    protected Class<DocumentRepositoryPortTypeProxy> getPortTypeClass() {
        return DocumentRepositoryPortTypeProxy.class;
    }

    public RegistryResponseType documentRepositoryProvideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType body) {
        try (final DocumentRepositoryPortTypeProxy port = createPort()) {
            final RegistryResponseType response = port.documentRepositoryProvideAndRegisterDocumentSetB(body);
            return response;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DS4PException(e);
        }
    }

    public RetrieveDocumentSetResponseType documentRepositoryRetrieveDocumentSet(RetrieveDocumentSetRequestType body) {
        try (final DocumentRepositoryPortTypeProxy port = createPort()) {
            final RetrieveDocumentSetResponseType response = port.documentRepositoryRetrieveDocumentSet(body);
            return response;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DS4PException(e);
        }
    }
}
