package gov.samhsa.c2s.pixclient.util;

public class PixManagerConstants {
	
	public static final String PIX_ADD = "Add";
	public static final String PIX_UPDATE = "Update";
	public static final String PIX_QUERY = "Query";
	public static final String ENCODE_STRING = "UTF-8";
	public static String GLOBAL_DOMAIN_ID;
	public static String GLOBAL_DOMAIN_NAME;
	
	public PixManagerConstants(String globalDomainId, String globalDomainName)
	{
		PixManagerConstants.GLOBAL_DOMAIN_ID = globalDomainId;
		PixManagerConstants.GLOBAL_DOMAIN_NAME = globalDomainName;
	}
}
