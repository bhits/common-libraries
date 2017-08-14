package gov.samhsa.acs.xdsb.repository.wsclient.adapter;

import java.util.List;

import gov.samhsa.acs.xdsb.repository.wsclient.XdsbRepositoryWebServiceClient;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

public class XdsbRepositoryAdapter {
	
	private XdsbRepositoryWebServiceClient xdsbRepository;
	
	public XdsbRepositoryAdapter(XdsbRepositoryWebServiceClient xdsbRepository) {
		this.xdsbRepository = xdsbRepository;
	}
	
	/**
	 * Entry point
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
	
	 public RetrieveDocumentSetResponseType documentRepositoryRetrieveDocumentSet(RetrieveDocumentSetRequestType body) {
		 return null;
	 }

}
