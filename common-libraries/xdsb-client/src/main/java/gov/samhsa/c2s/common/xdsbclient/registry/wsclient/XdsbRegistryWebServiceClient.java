package gov.samhsa.c2s.common.xdsbclient.registry.wsclient;

import gov.samhsa.c2s.common.cxf.AbstractEnhancedCxfClient;
import gov.samhsa.c2s.common.log.Logger;
import gov.samhsa.c2s.common.log.LoggerFactory;
import gov.samhsa.c2s.common.xdsbclient.exception.DS4PException;
import ihe.iti.xds_b._2007.DocumentRegistryService.DocumentRegistryPortTypeProxy;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import javax.xml.namespace.QName;
import java.net.URL;


public class XdsbRegistryWebServiceClient extends AbstractEnhancedCxfClient {
    private static final QName SERVICE_NAME = new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Service");
    private final Logger logger = LoggerFactory.getLogger(XdsbRegistryWebServiceClient.class);
    private final URL wsdlURL = this.getClass().getClassLoader().getResource("wsdl/XDS.b_DocumentRegistry.wsdl");

    public XdsbRegistryWebServiceClient(String endpointAddress) {
        super(SERVICE_NAME, endpointAddress);
        setWsAddressingEnabled(true);
    }

    public AdhocQueryResponse registryStoredQuery(AdhocQueryRequest input) {
        try (final DocumentRegistryPortTypeProxy port = createPort()) {
            final AdhocQueryResponse adhocQueryResponse = port.documentRegistryRegistryStoredQuery(input);
            return adhocQueryResponse;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DS4PException(e);
        }
    }

    @Override
    protected Class<DocumentRegistryPortTypeProxy> getPortTypeClass() {
        return DocumentRegistryPortTypeProxy.class;
    }
}
