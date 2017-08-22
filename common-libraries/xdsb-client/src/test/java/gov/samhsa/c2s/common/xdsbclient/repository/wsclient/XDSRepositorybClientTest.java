package gov.samhsa.c2s.common.xdsbclient.repository.wsclient;

import static org.junit.Assert.assertNotNull;

import java.net.URL;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPBinding;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StringUtils;

import ihe.iti.xds_b._2007.DocumentRepositoryService;
import ihe.iti.xds_b._2007.DocumentRepositoryService.DocumentRepositoryPortTypeProxy;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;

public class XDSRepositorybClientTest {
	private Endpoint ep;
	
	private final URL wsdlURL = this.getClass().getClassLoader().getResource("wsdl/XDS.b_DocumentRepository.wsdl");
	
	private final QName serviceName = new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Service");
	
	private String endpointAddress;
	
	private final String portNumber = "8888";
	
	@Before
	public void setUp() {
		try {
			endpointAddress = String.format("http://localhost:%s/services/xdsrepositoryb", portNumber);

			ep = Endpoint.publish(endpointAddress, new DocumentRepository_Port_Soap12Impl());

			assertNotNull(ep);

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
	public void testStubWebServiceWorks_provideAndRegisterDocumentSetRequest() throws JAXBException {
		// Arrange
		final SubmitObjectsRequest submitObjectRequest = new SubmitObjectsRequest();

		final Document document = new Document();
		document.setId("Document01");
		document.setValue("xyz".getBytes());

		final ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
		request.setSubmitObjectsRequest(submitObjectRequest);
		request.getDocument().add(document);

		// Act
		final Object response = createPort().documentRepositoryProvideAndRegisterDocumentSetB(request);

		// Assert
		assertNotNull(response);
	}
	
	@Test
	public void testStubWebServiceWorks_retrieveDocumentSetRequest() {
		// Arrange
		final RetrieveDocumentSetRequestType request = new RetrieveDocumentSetRequestType();
		final DocumentRequest documentRequest = new DocumentRequest();
		documentRequest.setHomeCommunityId("HC");
		documentRequest
				.setRepositoryUniqueId("1.3.6.1.4.1.21367.2010.1.2.1040");
		documentRequest.setDocumentUniqueId("$uniqueId06");
		request.getDocumentRequest().add(documentRequest);

		// Act
		final Object response = createPort().documentRepositoryRetrieveDocumentSet(request);

		// Assert
		assertNotNull(response);
	}
	
	
	private DocumentRepositoryPortTypeProxy createPort() {
		final DocumentRepositoryPortTypeProxy port = new DocumentRepositoryService(wsdlURL, serviceName).getDocumentRepositoryPortSoap12();
		if (StringUtils.hasText(this.endpointAddress)) {
			final BindingProvider bp = port;
			bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
			
			SOAPBinding binding = (SOAPBinding) bp.getBinding();
			binding.setMTOMEnabled(true);
		}
		
		return port;
	}
	
}
