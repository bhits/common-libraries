<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs"
                version="2.0">

<xsl:variable name="recepientSubjectNPI" select="XacmlRequest/RecipientNpi"  />
<xsl:variable name="intermediarySubjectNPI" select="XacmlRequest/IntermediaryNpi"  />
<xsl:variable name="purposeOfUse" select="XacmlRequest/PurposeOfUse"  />
<xsl:variable name="patientId" select="XacmlRequest/PatientId/Extension"  />


<xsl:template match="/">
    <Request xmlns="urn:oasis:names:tc:xacml:2.0:context:schema:os"     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
        <Subject>
            <Attribute AttributeId="urn:oasis:names:tc:xacml:1.0:subject-category:recipient-subject"       DataType="http://www.w3.org/2001/XMLSchema#string">
                <AttributeValue><xsl:value-of select="$recepientSubjectNPI" /></AttributeValue>
            </Attribute>
            <Attribute AttributeId="urn:oasis:names:tc:xacml:1.0:subject-category:intermediary-subject"       DataType="http://www.w3.org/2001/XMLSchema#string">
                <AttributeValue><xsl:value-of select="$intermediarySubjectNPI" /></AttributeValue>
            </Attribute>
            <Attribute AttributeId="gov.samhsa.consent2share.purpose-of-use-code"       DataType="http://www.w3.org/2001/XMLSchema#string">
                <AttributeValue><xsl:value-of select="$purposeOfUse" /></AttributeValue>
            </Attribute>
        </Subject>
        <Resource>
            <Attribute AttributeId="urn:oasis:names:tc:xacml:1.0:resource:typeCode"       DataType="http://www.w3.org/2001/XMLSchema#string">
                <AttributeValue>34133-9</AttributeValue>
            </Attribute>
            <Attribute AttributeId="xacml:status"     DataType="http://www.w3.org/2001/XMLSchema#string">
                <AttributeValue>Approved</AttributeValue>
            </Attribute>
        </Resource>
        <Action>
            <Attribute AttributeId="urn:oasis:names:tc:xacml:1.0:action:action-id"       DataType="http://www.w3.org/2001/XMLSchema#string">
                <AttributeValue>xdsquery</AttributeValue>
            </Attribute>
        </Action>
        <Environment>
            <Attribute AttributeId="urn:oasis:names:tc:xacml:1.0:environment:current-dateTime"       DataType="http://www.w3.org/2001/XMLSchema#dateTime">
                <AttributeValue>2015-08-18T16:52:15-0400</AttributeValue>
            </Attribute>
        </Environment>
    </Request>	</xsl:template>
</xsl:stylesheet>