package gov.samhsa.acs.xdsb.repository.wsclient.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.samhsa.acs.common.tool.XmlTransformer;
import gov.samhsa.acs.common.tool.XmlTransformerImpl;

import gov.samhsa.acs.xdsb.common.UniqueOidProviderImpl;
import gov.samhsa.acs.xdsb.common.XdsbDocumentType;
import gov.samhsa.acs.xdsb.common.XdsbMetadataGenerator;
import gov.samhsa.acs.xdsb.common.XdsbMetadataGeneratorImpl;
import gov.samhsa.acs.xdsb.repository.wsclient.XdsbRepositoryWebServiceClient;
import gov.samhsa.c2s.common.marshaller.SimpleMarshaller;
import gov.samhsa.c2s.common.marshaller.SimpleMarshallerException;
import gov.samhsa.c2s.common.marshaller.SimpleMarshallerImpl;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

public class XdsbRepositoryAdapter {

	private XdsbRepositoryWebServiceClient xdsbRepository;

	private SimpleMarshaller marshaller;
	
	private XmlTransformer xmlTransformer;
	
	public static final String EMPTY_XML_DOCUMENT = "<empty/>";
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public XdsbRepositoryAdapter(XdsbRepositoryWebServiceClient xdsbRepository, SimpleMarshaller marshaller) {
		this.xdsbRepository = xdsbRepository;
		this.marshaller = marshaller;
		this.xmlTransformer = new XmlTransformerImpl(new SimpleMarshallerImpl());
	}

	/**
	 * Entry point
	 * 
	 * @param documentUniqueId
	 * @param repositoryId
	 * @return
	 */
	public RetrieveDocumentSetResponseType retrieveDocumentSet(String documentUniqueId, String repositoryId) {
		final RetrieveDocumentSetRequestType request = new RetrieveDocumentSetRequestType();
		final DocumentRequest documentRequest = new DocumentRequest();

		documentRequest.setDocumentUniqueId(documentUniqueId);
		documentRequest.setRepositoryUniqueId(repositoryId);

		request.getDocumentRequest().add(documentRequest);

		return xdsbRepository.documentRepositoryRetrieveDocumentSet(request);

	}

	public RegistryResponseType documentRepositoryRetrieveDocumentSet(String documentXml, String homeCommunityId, XdsbDocumentType documentType, String patientId, String entryUUID) {
		final String submitObjectRequestXml = generateMetadata(documentXml, homeCommunityId, documentType, patientId, entryUUID);
		
		logger.info("homeCommunityId : " + homeCommunityId);
		logger.info("documentType : " + documentType);
		logger.info("patientId : " + patientId);
		logger.info("entryUUID : " + entryUUID);
		
		logger.info("submitObjectRequestXml : " + submitObjectRequestXml);
		
		SubmitObjectsRequest submitObjectRequest = null;
		try {
			submitObjectRequest = marshaller.unmarshalFromXml(SubmitObjectsRequest.class, submitObjectRequestXml);
		} catch (final SimpleMarshallerException e) {
			//throw e;
			e.printStackTrace();
		}
		
		Document document = null;
		if (!documentXml.equals(EMPTY_XML_DOCUMENT)) {
			document = createDocument(documentXml);
		}
		
		final ProvideAndRegisterDocumentSetRequestType request = createProvideAndRegisterDocumentSetRequest(submitObjectRequest, document);
		
		return xdsbRepository.documentRepositoryProvideAndRegisterDocumentSetB(request);
		

	}
	
	private ProvideAndRegisterDocumentSetRequestType createProvideAndRegisterDocumentSetRequest(SubmitObjectsRequest submitObjectRequest, Document document) {
		final ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
		request.setSubmitObjectsRequest(submitObjectRequest);
		if (document != null) {
			request.getDocument().add(document);
		}
		return request;
	}

	private String generateMetadata(String documentXmlString, String homeCommunityId, XdsbDocumentType documentType, String patientId, String entryUUID) {
		final XdsbMetadataGenerator xdsbMetadataGenerator = new XdsbMetadataGeneratorImpl(new UniqueOidProviderImpl(), documentType, this.marshaller, xmlTransformer);
		final String metadata = xdsbMetadataGenerator.generateMetadataXml(documentXmlString, homeCommunityId, patientId, entryUUID);
		return metadata;
	}
	
	private Document createDocument(String documentXmlString) {
		final Document document = new Document();
		document.setId("Document01");
		document.setValue(documentXmlString.getBytes());
		return document;
	}

}
