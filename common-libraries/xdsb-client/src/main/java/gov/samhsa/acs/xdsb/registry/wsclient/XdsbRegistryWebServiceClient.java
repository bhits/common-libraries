package gov.samhsa.acs.xdsb.registry.wsclient;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.apache.cxf.headers.Header;
import org.springframework.util.StringUtils;

import ihe.iti.xds_b._2007.DocumentRegistryService;
import ihe.iti.xds_b._2007.DocumentRegistryService.DocumentRegistryPortTypeProxy;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;



public class XdsbRegistryWebServiceClient {

	private final String endpointAddress;

	final URL wsdlURL = this.getClass().getClassLoader().getResource("wsdl/XDS.b_DocumentRegistry.wsdl");

	final QName serviceName = new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Service");
	
	final List<Header> headers = new ArrayList<Header>();

	public XdsbRegistryWebServiceClient(String endpointAddress) {
		this.endpointAddress = endpointAddress;
	}

	public AdhocQueryResponse registryStoredQuery(AdhocQueryRequest input) {
		try (DocumentRegistryPortTypeProxy port = createPortProxy()) {
			final AdhocQueryResponse adhocQueryResponse = port.documentRegistryRegistryStoredQuery(input);
			return adhocQueryResponse;
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private DocumentRegistryPortTypeProxy createPortProxy() {
		final DocumentRegistryPortTypeProxy port = new DocumentRegistryService(wsdlURL, serviceName).getDocumentRegistryPortSoap12();
		if (StringUtils.hasText(this.endpointAddress)) {
			final BindingProvider bp = port;
			bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
			//bp.getRequestContext().put(Header.HEADER_LIST, headers);
			
		}
		return port;
	}
	
	
}
