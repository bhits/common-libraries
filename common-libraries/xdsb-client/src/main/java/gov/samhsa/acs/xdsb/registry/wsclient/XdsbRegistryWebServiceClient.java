package gov.samhsa.acs.xdsb.registry.wsclient;

import java.net.URL;
import java.util.List;
import java.util.Optional;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;
import org.springframework.util.StringUtils;

import gov.samhsa.acs.common.cxf.AbstractCXFLoggingConfigurerClient;
import ihe.iti.xds_b._2007.DocumentRegistryService;
import ihe.iti.xds_b._2007.DocumentRegistryService.DocumentRegistryPortTypeProxy;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;



public class XdsbRegistryWebServiceClient extends AbstractCXFLoggingConfigurerClient {

	private final String endpointAddress;

	private final URL wsdlURL = this.getClass().getClassLoader().getResource("wsdl/XDS.b_DocumentRegistry.wsdl");

	private final QName serviceName = new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Service");
	
	private Optional<List<Interceptor<? extends Message>>> outInterceptors = Optional.empty();

	private Optional<List<Interceptor<? extends Message>>> inInterceptors = Optional.empty();
	
	public XdsbRegistryWebServiceClient(String endpointAddress) {
		this.endpointAddress = endpointAddress;
	}

	public AdhocQueryResponse registryStoredQuery(AdhocQueryRequest input) {
		final DocumentRegistryPortTypeProxy port = createPort();
		final AdhocQueryResponse adhocQueryResponse = port.documentRegistryRegistryStoredQuery(input);
		return adhocQueryResponse;
	}
	
	private DocumentRegistryPortTypeProxy createPort() {
		return configurePort(this::createPortProxy);
	}
	
	private DocumentRegistryPortTypeProxy createPortProxy() {
		final DocumentRegistryPortTypeProxy port = new DocumentRegistryService(wsdlURL, serviceName).getDocumentRegistryPortSoap12();
		if (StringUtils.hasText(this.endpointAddress)) {
			final BindingProvider bp = port;
			bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
			
		}
		return port;
	}
	
	/**
	 * Sets the in interceptors.
	 *
	 * @param inInterceptors
	 *            the new in interceptors
	 */
	public void setInInterceptors(List<Interceptor<? extends Message>> inInterceptors) {
		this.inInterceptors = Optional.of(inInterceptors);
	}

	/**
	 * Sets the out interceptors.
	 *
	 * @param outInterceptors
	 *            the new out interceptors
	 */
	public void setOutInterceptors(List<Interceptor<? extends Message>> outInterceptors) {
		this.outInterceptors = Optional.of(outInterceptors);
	}
	
	
}
