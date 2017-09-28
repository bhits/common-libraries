package gov.samhsa.c2s.common.xdsbclient.repository.wsclient.adapter;


import gov.samhsa.c2s.common.cxf.ContentTypeRebuildingOutboundSoapInterceptor;
import gov.samhsa.c2s.common.document.transformer.XmlTransformerImpl;
import gov.samhsa.c2s.common.filereader.FileReader;
import gov.samhsa.c2s.common.filereader.FileReaderImpl;
import gov.samhsa.c2s.common.marshaller.SimpleMarshallerImpl;
import gov.samhsa.c2s.common.xdsbclient.XdsbDocumentType;
import gov.samhsa.c2s.common.xdsbclient.repository.wsclient.XdsbRepositoryWebServiceClient;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.log4j.BasicConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class XdsbRepositoryAdapterIT {

    private static final String DEV_XDSB_REPOSITORY_ENDPOINT = "http://bhitsdevhie01:9080/axis2/services/xdsrepositoryb";
    private static final String documentUniqueId = "10114131599.911115.49715.10162.81191492108130511";
    private static final String repositoryId = "1.3.6.1.4.1.21367.2010.1.2.1040";
    private static final String OPENEMPI_GLOBAL_DOMAIN_ID = "2.16.840.1.113883.4.357";
    private static final String C2S_MRN_OID = "1.3.6.1.4.1.21367.13.20.200";
    private static final XdsbDocumentType XDSB_DOCUMENT_TYPE_CLINICAL_DOCUMENT = XdsbDocumentType.CLINICAL_DOCUMENT;
    private static final String SUCCESS = "Success";
    private static final String DOCUMENT_SUFFIX = "ISO";
    private static String endpointAddress;
    // System under test
    private static XdsbRepositoryAdapter xdsbRepositoryAdapter;
    private static FileReader fileReader;
    private static String c32;
    private static String CCDA11;
    private static String CCDA20;
    private static String CCDA21;
    private static String c32_with_c2s_mrn_oid;

    @BeforeClass
    public static void setUp() throws Exception {
        BasicConfigurator.configure();

        endpointAddress = DEV_XDSB_REPOSITORY_ENDPOINT;
        final XdsbRepositoryWebServiceClient client = new XdsbRepositoryWebServiceClient(endpointAddress);
        client.setOutInterceptors(Collections.singletonList(new ContentTypeRebuildingOutboundSoapInterceptor()));
        client.setLoggingInterceptorsEnabled(true);

        xdsbRepositoryAdapter = new XdsbRepositoryAdapter(client, new SimpleMarshallerImpl(), new XmlTransformerImpl(new SimpleMarshallerImpl()));


        fileReader = new FileReaderImpl();
        c32 = fileReader.readFile("uploadC32.xml");
        c32_with_c2s_mrn_oid = fileReader.readFile("uploadC32_C2S_OID.xml");
        CCDA11 = fileReader.readFile("C-CDA_R1.1.xml");
        CCDA20 = fileReader.readFile("C-CDA_R2.0.xml");
        CCDA21 = fileReader.readFile("C-CDA_R2.1.xml");
    }

    @Test
    public void testDocumentRepositoryRetrieveDocumentSet() throws Exception {
        RetrieveDocumentSetResponseType response = xdsbRepositoryAdapter.retrieveDocumentSet(documentUniqueId, repositoryId);

        assert (response.getDocumentResponse().size() > 0);
        assertNotNull(response.getRegistryResponse());
    }

    /**
     * This test publishes a doc (uploadC32.xml)
     * containing patient's EId and Global Domain ID(2.16.840.1.113883.4.357) to bhitsdevhie01
     * NOTE: Before publishing, update patientRole.id.extension in uploadC32.xml with the patient's Enterprise Id
     *
     * @throws Exception
     */
    @Test
    public void testProvideAndRegisterDocumentSet() throws Exception {
        RegistryResponseType response = xdsbRepositoryAdapter.documentRepositoryRetrieveDocumentSet(c32, OPENEMPI_GLOBAL_DOMAIN_ID, XDSB_DOCUMENT_TYPE_CLINICAL_DOCUMENT, DOCUMENT_SUFFIX);

        assertTrue(response.getStatus().contains(SUCCESS));
    }

    /**
     * This test publishes a doc (uploadC32_C2S_OID.xml)
     * containing C2S MRN and OID(1.3.6.1.4.1.21367.13.20.200) to bhitsdevhie01
     * NOTE: Before publishing, update patientRole.id.extension in uploadC32_C2S_OID.xml with the correct C2S MRN
     *
     * @throws Exception
     */
    @Test
    public void publishDocumentContainingC2SMRN() throws Exception {
        RegistryResponseType response = xdsbRepositoryAdapter.documentRepositoryRetrieveDocumentSet(c32_with_c2s_mrn_oid, C2S_MRN_OID, XDSB_DOCUMENT_TYPE_CLINICAL_DOCUMENT, DOCUMENT_SUFFIX);
        assertTrue(response.getStatus().contains(SUCCESS));
    }

    /**
     * Publish a CCDA 1.1 document
     *
     * @throws Exception
     */
    @Test
    public void testProvideAndRegisterDocumentSet_CCDA11() throws Exception {
        RegistryResponseType response = xdsbRepositoryAdapter.documentRepositoryRetrieveDocumentSet(CCDA11, OPENEMPI_GLOBAL_DOMAIN_ID, XDSB_DOCUMENT_TYPE_CLINICAL_DOCUMENT, DOCUMENT_SUFFIX);

        assertTrue(response.getStatus().contains(SUCCESS));
    }

    /**
     * Publish a CCDA 2.0 document
     *
     * @throws Exception
     */
    @Test
    public void testProviderAndRegisterDocumentSet_CCDA20() throws Exception {
        RegistryResponseType response = xdsbRepositoryAdapter.documentRepositoryRetrieveDocumentSet(CCDA20, OPENEMPI_GLOBAL_DOMAIN_ID, XDSB_DOCUMENT_TYPE_CLINICAL_DOCUMENT, DOCUMENT_SUFFIX);

        assertTrue(response.getStatus().contains(SUCCESS));
    }

    /**
     * Publish a CCDA 2.1 document
     *
     * @throws Exception
     */
    @Test
    public void testProviderAndRegisterDocumentSet_CCDA21() throws Exception {
        RegistryResponseType response = xdsbRepositoryAdapter.documentRepositoryRetrieveDocumentSet(CCDA21, OPENEMPI_GLOBAL_DOMAIN_ID, XDSB_DOCUMENT_TYPE_CLINICAL_DOCUMENT, DOCUMENT_SUFFIX);

        assertTrue(response.getStatus().contains(SUCCESS));
    }
}
