package gov.samhsa.acs.xdsb.repository.wsclient.adapter;

import java.util.Arrays;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.BeforeClass;
import org.junit.Test;

import gov.samhsa.acs.common.cxf.ContentTypeRebuildingOutboundSoapInterceptor;
import gov.samhsa.acs.xdsb.repository.wsclient.XdsbRepositoryWebServiceClient;
import ihe.iti.xds_b._2007.DocumentRepositoryService;

public class XdsbRepositoryAdapterIT {
	
	private static String endpointAddress;
	
	private static final String DEV_XDSB_REPOSITORY_ENDPOINT = "http://bhitsdevhie01:9080/axis2/services/xdsrepositoryb";
	
	private static final String documentUniqueId = "41421263015.98411.41414.91230.401390172014139";
	
	private static final String repositoryId = "1.3.6.1.4.1.21367.2010.1.2.1040";
	
	// System under test
	private static XdsbRepositoryAdapter xdsbRepositoryAdapter;
	
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
		
		xdsbRepositoryAdapter = new XdsbRepositoryAdapter(client);
		

		

	}
	
	@Test
	public void testDocumentRepositoryRetrieveDocumentSet () throws Exception {
		xdsbRepositoryAdapter.retrieveDocumentSet(documentUniqueId, repositoryId);
	}

}
