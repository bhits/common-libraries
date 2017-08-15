package gov.samhsa.acs.xdsb.repository.wsclient.adapter;


import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import gov.samhsa.acs.common.cxf.ContentTypeRebuildingOutboundSoapInterceptor;
import gov.samhsa.acs.xdsb.common.XdsbDocumentType;
import gov.samhsa.acs.xdsb.repository.wsclient.XdsbRepositoryWebServiceClient;
import gov.samhsa.c2s.common.filereader.FileReader;
import gov.samhsa.c2s.common.filereader.FileReaderImpl;
import gov.samhsa.c2s.common.marshaller.SimpleMarshallerImpl;

public class XdsbRepositoryAdapterIT {
	
	private static String endpointAddress;
	
	private static final String DEV_XDSB_REPOSITORY_ENDPOINT = "http://bhitsdevhie01:9080/axis2/services/xdsrepositoryb";
	
	private static final String documentUniqueId = "41421263015.98411.41414.91230.401390172014139";
	
	private static final String repositoryId = "1.3.6.1.4.1.21367.2010.1.2.1040";
	
	private static final String OPENEMPI_DOMAIN_ID = "2.16.840.1.113883.4.357";
	private static final XdsbDocumentType XDSB_DOCUMENT_TYPE_CLINICAL_DOCUMENT = XdsbDocumentType.CLINICAL_DOCUMENT;
	
	// System under test
	private static XdsbRepositoryAdapter xdsbRepositoryAdapter;
	
	private static FileReader fileReader;
	private static String c32;
	
	@BeforeClass
	public static void setUp() throws Exception {
		System.setProperty("http.proxyHost", "127.0.0.1");
		System.setProperty("https.proxyHost", "127.0.0.1");
		System.setProperty("http.proxyPort", "8888");
		System.setProperty("https.proxyPort", "8888");
		
		endpointAddress = DEV_XDSB_REPOSITORY_ENDPOINT;
		final XdsbRepositoryWebServiceClient client = new XdsbRepositoryWebServiceClient(endpointAddress);
		client.setOutInterceptors(Arrays.asList(new ContentTypeRebuildingOutboundSoapInterceptor()));
		client.setEnableLoggingInterceptors(true);
		
		xdsbRepositoryAdapter = new XdsbRepositoryAdapter(client, new SimpleMarshallerImpl());
		

		fileReader = new FileReaderImpl();
		c32 = fileReader.readFile("uploadC32.xml");

	}
	
	@Test
	public void testDocumentRepositoryRetrieveDocumentSet() throws Exception {
		xdsbRepositoryAdapter.retrieveDocumentSet(documentUniqueId, repositoryId);
	}
	
	@Test
	public void testProvideAndRegisterDocumentSet() throws Exception {
		xdsbRepositoryAdapter.documentRepositoryRetrieveDocumentSet(c32, OPENEMPI_DOMAIN_ID, XDSB_DOCUMENT_TYPE_CLINICAL_DOCUMENT, null, null);
	} 

}
