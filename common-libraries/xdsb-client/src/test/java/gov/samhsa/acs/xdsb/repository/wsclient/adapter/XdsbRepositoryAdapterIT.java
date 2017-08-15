package gov.samhsa.acs.xdsb.repository.wsclient.adapter;

import gov.samhsa.acs.common.cxf.ContentTypeRebuildingOutboundSoapInterceptor;
import gov.samhsa.acs.xdsb.repository.wsclient.XdsbRepositoryWebServiceClient;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

public class XdsbRepositoryAdapterIT {

    private static String endpointAddress;

    private static final String DEV_XDSB_REPOSITORY_ENDPOINT = "http://bhitsdevhie01:9080/axis2/services/xdsrepositoryb";

    private static final String documentUniqueId = "129.6.58.92.2033185";

    private static final String repositoryId = "1.3.6.1.4.1.21367.2010.1.2.1040";

    // System under test
    private static XdsbRepositoryAdapter xdsbRepositoryAdapter;

    @BeforeClass
    public static void setUp() throws Exception {
        endpointAddress = DEV_XDSB_REPOSITORY_ENDPOINT;
        final XdsbRepositoryWebServiceClient client = new XdsbRepositoryWebServiceClient(endpointAddress);
        client.setOutInterceptors(Arrays.asList(new ContentTypeRebuildingOutboundSoapInterceptor()));
        client.setEnableLoggingInterceptors(true);

        xdsbRepositoryAdapter = new XdsbRepositoryAdapter(client);


    }

    @Test
    public void testDocumentRepositoryRetrieveDocumentSet() throws Exception {
        RetrieveDocumentSetResponseType response = xdsbRepositoryAdapter.retrieveDocumentSet(documentUniqueId, repositoryId);
    }

}
