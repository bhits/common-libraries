package gov.samhsa.c2s.common.xdsbclient;

/**
 * The enum for the supported XDS.b document types by the
 * PolicyEnforcementPoint.
 */
public enum XdsbDocumentType {

	/** The clinical document. */
	CLINICAL_DOCUMENT,
	/** The privacy consent. */
	PRIVACY_CONSENT,
	/** The deprecate privacy consent. */
	DEPRECATE_PRIVACY_CONSENT;
}
