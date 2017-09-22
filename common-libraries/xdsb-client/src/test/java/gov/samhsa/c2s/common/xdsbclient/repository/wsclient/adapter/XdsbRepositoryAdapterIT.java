package gov.samhsa.c2s.common.xdsbclient.repository.wsclient.adapter;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import gov.samhsa.c2s.common.filereader.FileReaderImpl;
import gov.samhsa.c2s.common.marshaller.SimpleMarshallerImpl;
import gov.samhsa.c2s.common.xdsbclient.XdsbDocumentType;
import gov.samhsa.c2s.common.xdsbclient.cxf.ContentTypeRebuildingOutboundSoapInterceptor;
import gov.samhsa.c2s.common.xdsbclient.repository.wsclient.XdsbRepositoryWebServiceClient;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import gov.samhsa.c2s.common.document.transformer.XmlTransformerImpl;
import gov.samhsa.c2s.common.filereader.FileReader;

public class XdsbRepositoryAdapterIT {
	
	private static String endpointAddress;
	
	private static final String DEV_XDSB_REPOSITORY_ENDPOINT = "http://bhitsdevhie01:9080/axis2/services/xdsrepositoryb";
	
	private static final String documentUniqueId = "10114131599.911115.49715.10162.81191492108130511";
	
	private static final String repositoryId = "1.3.6.1.4.1.21367.2010.1.2.1040";
	
	private static final String OPENEMPI_DOMAIN_ID = "2.16.840.1.113883.4.357";
	
	private static final XdsbDocumentType XDSB_DOCUMENT_TYPE_CLINICAL_DOCUMENT = XdsbDocumentType.CLINICAL_DOCUMENT;
	
	private static final String SUCCESS = "Success";
	
	// System under test
	private static XdsbRepositoryAdapter xdsbRepositoryAdapter;
	
	private static FileReader fileReader;
	
	private static String c32;

	private static String CCDA11;
	private static String CCDA20;
	private static String CCDA21;
	
	@BeforeClass
	public static void setUp() throws Exception {
		BasicConfigurator.configure();
		
		endpointAddress = DEV_XDSB_REPOSITORY_ENDPOINT;
		final XdsbRepositoryWebServiceClient client = new XdsbRepositoryWebServiceClient(endpointAddress);
		client.setOutInterceptors(Arrays.asList(new ContentTypeRebuildingOutboundSoapInterceptor()));
		client.setEnableLoggingInterceptors(true);
		
		xdsbRepositoryAdapter = new XdsbRepositoryAdapter(client, new SimpleMarshallerImpl(), new XmlTransformerImpl(new SimpleMarshallerImpl()));
		

		fileReader = new FileReaderImpl();
		c32 = fileReader.readFile("uploadC32.xml");


		CCDA11 = fileReader.readFile("C-CDA_R1.1.xml");
		CCDA20 = fileReader.readFile("C-CDA_R2.0.xml");
		CCDA21 = fileReader.readFile("C-CDA_R2.1.xml");

	}

	/**
	 * Retrieve a document for a given documentUniqueId and repositoryId
	 * @throws Exception
	 */
	@Test
	public void testDocumentRepositoryRetrieveDocumentSet() throws Exception {
		RetrieveDocumentSetResponseType response = xdsbRepositoryAdapter.retrieveDocumentSet(documentUniqueId, repositoryId);
		
		assert(response.getDocumentResponse().size() > 0);
		assertNotNull(response.getRegistryResponse());
	}

	/**
	 * Publish a C32 document
	 * @throws Exception
	 */
	@Test
	public void testProvideAndRegisterDocumentSet() throws Exception {
		RegistryResponseType response = xdsbRepositoryAdapter.documentRepositoryRetrieveDocumentSet(c32, OPENEMPI_DOMAIN_ID, XDSB_DOCUMENT_TYPE_CLINICAL_DOCUMENT);
		
		assertTrue(response.getStatus().contains(SUCCESS));
	}

	/**
	 * Publish a CCDA 1.1 document
	 * @throws Exception
	 */
	@Test
	public void testProvideAndRegisterDocumentSet_CCDA11() throws Exception {
		RegistryResponseType response = xdsbRepositoryAdapter.documentRepositoryRetrieveDocumentSet(CCDA11, OPENEMPI_DOMAIN_ID, XDSB_DOCUMENT_TYPE_CLINICAL_DOCUMENT);

		assertTrue(response.getStatus().contains(SUCCESS));
	}

	/**
	 * Publish a CCDA 2.0 document
	 * @throws Exception
	 */
	@Test
	public void testProviderAndRegisterDocumentSet_CCDA20()  throws Exception {
		RegistryResponseType response = xdsbRepositoryAdapter.documentRepositoryRetrieveDocumentSet(CCDA20, OPENEMPI_DOMAIN_ID, XDSB_DOCUMENT_TYPE_CLINICAL_DOCUMENT);

		assertTrue(response.getStatus().contains(SUCCESS));
	}

	/**
	 * Publish a CCDA 2.1 document
	 * @throws Exception
	 */
	@Test
	public void testProviderAndRegisterDocumentSet_CCDA21() throws Exception {
		RegistryResponseType response = xdsbRepositoryAdapter.documentRepositoryRetrieveDocumentSet(CCDA21, OPENEMPI_DOMAIN_ID, XDSB_DOCUMENT_TYPE_CLINICAL_DOCUMENT);

		assertTrue(response.getStatus().contains(SUCCESS));
	}

}
