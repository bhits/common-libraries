package gov.samhsa.c2s.common.xdsbclient.registry.wsclient.adapter;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.samhsa.c2s.common.xdsbclient.XdsbDocumentType;
import gov.samhsa.c2s.common.xdsbclient.cxf.ContentTypeRebuildingOutboundSoapInterceptor;
import gov.samhsa.c2s.common.xdsbclient.registry.wsclient.XdsbRegistryWebServiceClient;
import gov.samhsa.c2s.common.xdsbclient.registry.wsclient.adapter.XdsbRegistryAdapter;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

public class XdsbRegistryAdapterIT {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String DEV_XDSB_REGISTRY_ENDPOINT = "http://bhitsdevhie01:9080/axis2/services/xdsregistryb";
	
	private static final String PATIENT_ID = "d3bb3930-7241-11e3-b4f7-00155d3a2124^^^&2.16.840.1.113883.4.357&ISO";
	
	private static String endpointAddress;
	
	private static String SUCCESS = "Success";
	
	// System under test
	private static XdsbRegistryAdapter xdsbRegistryAdapter;
	
	@BeforeClass
	public static void setUp() throws Exception {
		BasicConfigurator.configure();
		
		endpointAddress = DEV_XDSB_REGISTRY_ENDPOINT;
		final XdsbRegistryWebServiceClient client = new XdsbRegistryWebServiceClient(endpointAddress);
		client.setOutInterceptors(Arrays.asList(new ContentTypeRebuildingOutboundSoapInterceptor()));
		client.setEnableLoggingInterceptors(true);
		
		xdsbRegistryAdapter = new XdsbRegistryAdapter(client);

	}
	
	@Test
	public void testRegistryStoredQuery() throws Exception {
		AdhocQueryResponse response = xdsbRegistryAdapter.registryStoredQuery(PATIENT_ID, XdsbDocumentType.CLINICAL_DOCUMENT);
		
		assertTrue(response.getStatus().contains(SUCCESS));
	}

}
