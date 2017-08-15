package gov.samhsa.acs.xdsb.registry.wsclient.adapter;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.samhsa.acs.xdsb.common.XdsbDocumentType;
import gov.samhsa.acs.xdsb.registry.wsclient.XdsbRegistryWebServiceClient;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

public class XdsbRegistryAdapterIT {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String DEV_XDSB_REGISTRY_ENDPOINT = "http://bhitsdevhie01:9080/axis2/services/xdsregistryb";
	
	private static final String PATIENT_ID = "ac4afda28f60407^^^&1.3.6.1.4.1.21367.2005.3.7&ISO";
	
	private static String endpointAddress;
	
	private static String SUCCESS = "Success";
	
	// System under test
	private static XdsbRegistryAdapter xdsbRegistryAdapter;
	
	@BeforeClass
	public static void setUp() throws Exception {
		endpointAddress = DEV_XDSB_REGISTRY_ENDPOINT;
		xdsbRegistryAdapter = new XdsbRegistryAdapter(new XdsbRegistryWebServiceClient(endpointAddress));
	}
	
	@Test
	public void testRegistryStoredQuery() throws Exception {
		AdhocQueryResponse response = xdsbRegistryAdapter.registryStoredQuery(PATIENT_ID, XdsbDocumentType.CLINICAL_DOCUMENT);
		
		assertTrue(response.getStatus().contains(SUCCESS));
	}

}
