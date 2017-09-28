package gov.samhsa.c2s.common.xdsbclient;

import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;

/**
 * The Interface XdsbMetadataGenerator.
 */
public interface XdsbMetadataGenerator {

	/**
	 * Generate metadata xml.
	 * 
	 * @param document
	 *            the document (Pass XdsbRepositoryAdapter.EMPTY_XML_DOCUMENT if
	 *            deprecating a document. Otherwise, pass the actual document to
	 *            be provided.)
	 * @param homeCommunityId
	 *            the home community id (May pass null if deprecating a
	 *            document.)
	 * @return the string
	 */
	public String generateMetadataXml(String document, String homeCommunityId, String documentSuffix);



	/**
	 * Generate metadata.
	 * 
	 * @param document
	 *            the document (Pass XdsbRepositoryAdapter.EMPTY_XML_DOCUMENT if
	 *            deprecating a document. Otherwise, pass the actual document to
	 *            be provided.)
	 * @param homeCommunityId
	 *            the home community id (May pass null if deprecating a
	 *            document.)
	 * @return the submit objects request
	 */
	public SubmitObjectsRequest generateMetadata(String document, String homeCommunityId, String documentSuffix);
}
