package gov.samhsa.c2s.common.xdsbclient.registry.wsclient;

import static org.junit.Assert.assertNotNull;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Endpoint;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StringUtils;

import gov.samhsa.c2s.common.filereader.FileReader;
import gov.samhsa.c2s.common.filereader.FileReaderImpl;
import ihe.iti.xds_b._2007.DocumentRegistryService;
import ihe.iti.xds_b._2007.DocumentRegistryService.DocumentRegistryPortTypeProxy;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

public class XdsbRegistryWebServiceClientTest {
	
	private Endpoint ep;
	
	private String endpointAddress;
	
	private final URL wsdlURL = this.getClass().getClassLoader().getResource("wsdl/XDS.b_DocumentRegistry.wsdl");
	
	private final QName serviceName = new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Service");
	
	private final String portNumber = "8888";
	
	private FileReader fileReader;

	
	@Before
	public void setUp() {
		fileReader = new FileReaderImpl();
		try {
			endpointAddress = String.format("http://localhost:%s/services/xdsregistryb", portNumber);

			ep = Endpoint.publish(endpointAddress, new DocumentRegistry_Port_Soap12Impl());


		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() {
		try {
			ep.stop();
		} catch (final Throwable t) {
			System.out.println("Error thrown: " + t.getMessage());
		}
	}
	
	@Test
	public void testWSClientSOAPCallWorks_retrieveDocumentSetRequest() {
		// Arrange
		final AdhocQueryRequest request = new AdhocQueryRequest();

		// Act
		AdhocQueryResponse response = createPort().documentRegistryRegistryStoredQuery(request);
		
		// Assert
		assertNotNull(response);
	}
	
	private DocumentRegistryPortTypeProxy createPort() {
		final DocumentRegistryPortTypeProxy port = new DocumentRegistryService(wsdlURL, serviceName).getDocumentRegistryPortSoap12();
		if (StringUtils.hasText(this.endpointAddress)) {
			final BindingProvider bp = port;
			bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
			
		}
		return port;
	}
	
	

}
