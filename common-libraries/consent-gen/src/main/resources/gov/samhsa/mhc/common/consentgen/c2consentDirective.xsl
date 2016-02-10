<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs" version="2.0"  xmlns:mhc="http://mhc" xmlns="urn:hl7-org:v3"  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

    <xsl:output indent="yes"/>

    <xsl:variable name="currentDateTimeUtc" select="string((format-dateTime(current-dateTime(),'[Y0001][M01][D01][H01][m01][s01][z0001]')))"/>
    <xsl:variable name="effectivelowUTC" select="/ConsentExport/consentStart"/>
    <xsl:variable name="effectivehighUTC" select="/ConsentExport/consentEnd"/>

    <xsl:function name="mhc:transformDateTime" as="xs:string">
        <xsl:param name="dateTimeUtc"/>
        <xsl:variable name="dateTimeTemp" select="translate($dateTimeUtc,':T.GM','')"/>
        <xsl:value-of select="concat((translate(substring($dateTimeTemp, 1, 10),'-','')),substring($dateTimeTemp, 11))"/>
    </xsl:function>

    <xsl:variable name="patient" select="//ConsentExport/Patient"/>

    <xsl:variable name="providersDisclosureIsMadeTo" select="//IndividualProvidersDisclosureIsMadeToList"/>
    <xsl:variable name="organizationalProvidersDisclosureIsMadeTo" select="//OrganizationalProvidersDisclosureIsMadeToList"/>
    
    <xsl:variable name="consentId" select="ConsentExport/id"/>
    <xsl:variable name="patientId" select="ConsentExport/Patient/email"/>
    <xsl:variable name="policyId" select="concat('urn:samhsa:names:tc:consent2share:1.0:policyid:', $patientId, ':', $consentId)"/>


    <xsl:variable name="doNotShareSensitivityPolicyCodesList">
        <xsl:for-each
                select="//doNotShareSensitivityPolicyCodesList/doNotShareSensitivityPolicyCodes">
            <xsl:variable name="code" select="code"/>
            <xsl:variable name="displayName" select="displayName"/>
        <entryRelationship typeCode="COMP"
                           contextConductionInd="true">
            <organizer classCode="CLUSTER" moodCode="DEF">
                <!-- InformationCriteriaReferencesOrganizer template for all the
                    criteria used to select the relevant informatiom from the patient's record -->
                <templateId root="2.16.840.1.113883.3.445.9" />
                <statusCode code="active" />
                <component typeCode="COMP">
                    <!-- Related Condition/Problem -->
                    <observation classCode="OBS" moodCode="DEF">
                        <!--CriteriumRelatedProtectedProblem template -->
                        <templateId root="2.16.840.1.113883.3.445.11" />
                        <!-- ProblemObservation template from Consolidated CDA -->
                        <templateId root="2.16.840.1.113883.10.20.22.4.4" />
                        <code code="8319008" codeSystemName="SNOMED-CT"
                              codeSystem="2.16.840.1.113883.6.96" displayName="Principal diagnosis" />
                        <value xsi:type="CD" code="{$code}" codeSystemName="ActInformationSensitivityPolicy"
                               codeSystem="2.16.840.1.113883.1.11.20428" displayName="{$displayName}" />

                    </observation>
                </component>
                <component>
                    <!-- Confidentiality expressed as a security observation -->
                    <observation classCode="OBS" moodCode="EVN">
                        <!-- Security Observation -->
                        <templateId root="2.16.840.1.113883.3.445.21"
                                    assigningAuthorityName="HL7 CBCC" />
                        <!-- Confidentiality Code template -->
                        <templateId root="2.16.840.1.113883.3.445.12"
                                    assigningAuthorityName="HL7 CBCC" />
                        <!-- Confidentiality Security Observation - the only mandatory
                            element of a Privacy Annotation -->
                        <code code="SECCLASSOBS" codeSystem="2.16.840.1.113883.1.11.20457"
                              displayName="Security Classification" codeSystemName="HL7 SecurityObservationTypeCodeSystem" />
                        <!-- value set constrained to "2.16.840.1.113883.1.11.16926" -->
                        <value xsi:type="CE" code="R" codeSystem="2.16.840.1.113883.5.1063"
                               codeSystemName="SecurityObservationValueCodeSystem"
                               displayName="Restricted">
                            <originalText>Restricted Confidentiality</originalText>
                        </value>
                    </observation>
                </component>
            </organizer>
        </entryRelationship>
        </xsl:for-each>

    </xsl:variable>
    <xsl:variable name="doNotShareClinicalDocumentSectionTypeCodesList">
        <xsl:for-each
                select="//doNotShareClinicalDocumentSectionTypeCodesList/doNotShareClinicalDocumentSectionTypeCodes">

            <xsl:variable name="code" select="code"/>
            <xsl:variable name="displayName" select="displayName"/>
            <entryRelationship typeCode="COMP"
                               contextConductionInd="true">
                <organizer classCode="CLUSTER" moodCode="DEF">
                    <!-- InformationCriteriaReferencesOrganizer template for all the
                        criteria used to select the relevant informatiom from the patient's record -->
                    <templateId root="2.16.840.1.113883.3.445.9" />
                    <statusCode code="active" />
                    <component typeCode="COMP">
                        <!-- Related Condition/Problem -->
                        <observation classCode="OBS" moodCode="DEF">
                            <!--CriteriumRelatedProtectedProblem template -->
                            <templateId root="2.16.840.1.113883.3.445.11" />
                            <!-- ProblemObservation template from Consolidated CDA -->
                            <templateId root="2.16.840.1.113883.10.20.22.4.4" />
                            <code code="8319008" codeSystemName="SNOMED-CT"
                                  codeSystem="2.16.840.1.113883.6.96" displayName="Principal diagnosis" />
                            <value xsi:type="CD" code="{$code}" codeSystemName="LOINC"
                                   codeSystem="2.16.840.1.113883.6.1" displayName="{$displayName}" />

                        </observation>
                    </component>
                    <component>
                        <!-- Confidentiality expressed as a security observation -->
                        <observation classCode="OBS" moodCode="EVN">
                            <!-- Security Observation -->
                            <templateId root="2.16.840.1.113883.3.445.21"
                                        assigningAuthorityName="HL7 CBCC" />
                            <!-- Confidentiality Code template -->
                            <templateId root="2.16.840.1.113883.3.445.12"
                                        assigningAuthorityName="HL7 CBCC" />
                            <!-- Confidentiality Security Observation - the only mandatory
                                element of a Privacy Annotation -->
                            <code code="SECCLASSOBS" codeSystem="2.16.840.1.113883.1.11.20457"
                                  displayName="Security Classification" codeSystemName="HL7 SecurityObservationTypeCodeSystem" />
                            <!-- value set constrained to "2.16.840.1.113883.1.11.16926" -->
                            <value xsi:type="CE" code="R" codeSystem="2.16.840.1.113883.5.1063"
                                   codeSystemName="SecurityObservationValueCodeSystem"
                                   displayName="Restricted">
                                <originalText>Restricted Confidentiality</originalText>
                            </value>
                        </observation>
                    </component>
                </organizer>
            </entryRelationship>
        </xsl:for-each>

    </xsl:variable>


    <xsl:variable name="purposeofuse">
        <xsl:for-each
                select="//shareForPurposeOfUseCodesList/shareForPurposeOfUseCodes">
            <xsl:variable name="code" select="code"/>
            <xsl:variable name="displayName" select="displayName"/>
            <entry typeCode="COMP">
                <templateId root="2.16.840.1.113883.3.445.4" />
            <act classCode="ACT" moodCode="DEF">
            <templateId root="2.16.840.1.113883.3.445.5" />
            <!-- Purpose of use -->
                <code code="{$code}" codeSystem="2.16.840.1.113883.3.18.7.1"
                      codeSystemName="HL7 Purpose or Use" displayName="{$displayName}" />
            <!-- Action -->
            <entryRelationship typeCode="COMP"
                               contextConductionInd="true">
                <templateId root="2.16.840.1.113883.3.445.8" />
                <!-- negationInd='false' specifies that the action is authorized -->
                <observation classCode="OBS" moodCode="DEF"
                             negationInd="false">
                    <!-- Action/Operation -->
                    <code code="IDISCL" codeSystem="2.16.840.1.113883.5.4"
                          displayName="Disclose" codeSystemName="ActConsentType" />
                </observation>
            </entryRelationship>
            <xsl:sequence select="$doNotShareClinicalDocumentSectionTypeCodesList"/>
            <xsl:sequence select="$doNotShareSensitivityPolicyCodesList"/>
            </act>
        </entry>
        </xsl:for-each>
    </xsl:variable>

    <xsl:variable name="custodian">
    <xsl:for-each select="//IndividualProvidersPermittedToDiscloseList/IndividualProvidersPermittedToDisclose">
        <custodian>
            <assignedCustodian>
                <representedCustodianOrganization>
                    <id root="1.3.6.4.1.4.1.2835.2" extension="npi"/>
                    <name><xsl:value-of select="concat(firstName,' ', lastName)"/></name>
                    <addr>
                        <streetAddressLine>
                            <xsl:value-of select="firstLinePracticeLocationAddress"/>
                        </streetAddressLine>
                        <city>
                            <xsl:value-of select="practiceLocationAddressCityName"/>
                        </city>
                        <state>
                            <xsl:value-of select="practiceLocationAddressStateName"/>
                        </state>
                        <postalCode>
                            <xsl:value-of select="practiceLocationAddressPostalCode"/>
                        </postalCode>
                    </addr>
                </representedCustodianOrganization>
            </assignedCustodian>
        </custodian>
    </xsl:for-each>
    <xsl:for-each
            select="//OrganizationalProvidersPermittedToDiscloseList/OrganizationalProvidersPermittedToDisclose">
        <custodian>
            <assignedCustodian>
                <representedCustodianOrganization>
                    <id root="1.3.6.4.1.4.1.2835.2" extension="npi"/>
                    <name><xsl:value-of select="orgName"/></name>
                    <addr>
                        <streetAddressLine>
                            <xsl:value-of select="firstLinePracticeLocationAddress"/>
                        </streetAddressLine>
                        <city>
                            <xsl:value-of select="practiceLocationAddressCityName"/>
                        </city>
                        <state>
                            <xsl:value-of select="practiceLocationAddressStateName"/>
                        </state>
                        <postalCode>
                            <xsl:value-of select="practiceLocationAddressPostalCode"/>
                        </postalCode>
                    </addr>
                </representedCustodianOrganization>
            </assignedCustodian>
        </custodian>
    </xsl:for-each>
    </xsl:variable>

    
    <xsl:variable name="xacml">
        
        <Policy xmlns="urn:oasis:names:tc:xacml:3.0:core:schema:wd-17" PolicyId="{$consentId}"
            RuleCombiningAlgId="urn:oasis:names:tc:xacml:3.0:rule-combining-algorithm:deny-overrides"
            Version="1.0">
            <Description>This is a reference policy for <xsl:value-of select="$patientId"
            /></Description>/*-
            
            <Target/>
            
            <Rule Effect="Permit">
                <xsl:attribute name="RuleId">
                    <xsl:value-of select="$consentId"/>
                </xsl:attribute>
                <Target>
                    <AnyOf>
                        <xsl:for-each
                            select="//IndividualProvidersPermittedToDiscloseList/IndividualProvidersPermittedToDisclose">
                            
                            <AllOf>
                                <Match MatchId="urn:oasis:names:tc:xacml:1.0:function:string-equal">
                                    <AttributeValue
                                        DataType="http://www.w3.org/2001/XMLSchema#string">
                                        <xsl:value-of select="npi"/>
                                    </AttributeValue>
                                    <AttributeDesignator MustBePresent="false"
                                        Category="urn:oasis:names:tc:xacml:1.0:subject-category:access-subject"
                                        AttributeId="urn:samhsa:names:tc:consent2share:1.0:subject:provider-npi"
                                        DataType="http://www.w3.org/2001/XMLSchema#string"/>
                                </Match>
                            </AllOf>
                            
                        </xsl:for-each>
                        <xsl:for-each
                            select="//OrganizationalProvidersPermittedToDiscloseList/OrganizationalProvidersPermittedToDisclose">
                            
                            <AllOf>
                                <Match MatchId="urn:oasis:names:tc:xacml:1.0:function:string-equal">
                                    <AttributeValue
                                        DataType="http://www.w3.org/2001/XMLSchema#string">
                                        <xsl:value-of select="npi"/>
                                    </AttributeValue>
                                    <AttributeDesignator MustBePresent="false"
                                        Category="urn:oasis:names:tc:xacml:1.0:subject-category:access-subject"
                                        AttributeId="urn:samhsa:names:tc:consent2share:1.0:subject:provider-npi"
                                        DataType="http://www.w3.org/2001/XMLSchema#string"/>
                                </Match>
                            </AllOf>
                            
                        </xsl:for-each>
                        <xsl:for-each
                            select="//IndividualProvidersDisclosureIsMadeToList/IndividualProvidersDisclosureIsMadeTo">
                            
                            <AllOf>
                                <Match MatchId="urn:oasis:names:tc:xacml:1.0:function:string-equal">
                                    <AttributeValue
                                        DataType="http://www.w3.org/2001/XMLSchema#string">
                                        <xsl:value-of select="npi"/>
                                    </AttributeValue>
                                    <AttributeDesignator MustBePresent="false"
                                        Category="urn:oasis:names:tc:xacml:1.0:subject-category:recipient-subject"
                                        AttributeId="urn:samhsa:names:tc:consent2share:1.0:subject:provider-npi"
                                        DataType="http://www.w3.org/2001/XMLSchema#string"/>
                                </Match>
                            </AllOf>
                            
                        </xsl:for-each>
                        <xsl:for-each
                            select="//OrganizationalProvidersDisclosureIsMadeToList/OrganizationalProvidersDisclosureIsMadeTo">
                            
                            <AllOf>
                                <Match MatchId="urn:oasis:names:tc:xacml:1.0:function:string-equal">
                                    <AttributeValue
                                        DataType="http://www.w3.org/2001/XMLSchema#string">
                                        <xsl:value-of select="npi"/>
                                    </AttributeValue>
                                    <AttributeDesignator MustBePresent="false"
                                        Category="urn:oasis:names:tc:xacml:1.0:subject-category:recipient-subject"
                                        AttributeId="urn:samhsa:names:tc:consent2share:1.0:subject:provider-npi"
                                        DataType="http://www.w3.org/2001/XMLSchema#string"/>
                                </Match>
                            </AllOf>
                            
                        </xsl:for-each>
                    </AnyOf>
                    <AnyOf>
                        <xsl:for-each
                            select="//shareForPurposeOfUseCodesList/shareForPurposeOfUseCodes">
                            
                            <AllOf>
                                <Match MatchId="urn:oasis:names:tc:xacml:1.0:function:string-equal">
                                    <AttributeValue
                                        DataType="http://www.w3.org/2001/XMLSchema#string">
                                        <xsl:value-of select="code"/>
                                    </AttributeValue>
                                    <AttributeDesignator MustBePresent="false"
                                        Category="urn:oasis:names:tc:xacml:1.0:subject-category:recipient-subject"
                                        AttributeId="urn:samhsa:names:tc:consent2share:1.0:purpose-of-use-code"
                                        DataType="http://www.w3.org/2001/XMLSchema#string"/>
                                </Match>
                            </AllOf>
                            
                        </xsl:for-each>
                    </AnyOf>
                    <AnyOf>
                        <AllOf>
                            <Match MatchId="urn:oasis:names:tc:xacml:1.0:function:string-equal">
                                <AttributeValue DataType="http://www.w3.org/2001/XMLSchema#string">
                                    <!--This could also be a patient unique identifer. Using email for now.-->
                                    <xsl:value-of select="ConsentExport/Patient/email"/>
                                </AttributeValue>
                                <AttributeDesignator MustBePresent="false"
                                    Category="urn:oasis:names:tc:xacml:3.0:attribute-category:resource"
                                    AttributeId="urn:oasis:names:tc:xacml:1.0:resource:resource-id"
                                    DataType="http://www.w3.org/2001/XMLSchema#string"/>
                            </Match>
                        </AllOf>
                    </AnyOf>
                    
                    
                    
                    <AnyOf>
                        <AllOf>
                            <Match MatchId="urn:oasis:names:tc:xacml:1.0:function:string-equal">
                                <AttributeValue DataType="http://www.w3.org/2001/XMLSchema#string"
                                    >write</AttributeValue>
                                <AttributeDesignator MustBePresent="false"
                                    Category="urn:oasis:names:tc:xacml:3.0:attribute-category:action"
                                    AttributeId="urn:oasis:names:tc:xacml:1.0:action:action-id"
                                    DataType="http://www.w3.org/2001/XMLSchema#string"/>
                            </Match>
                        </AllOf>
                    </AnyOf>
                    <AnyOf>
                        <xsl:for-each select="//consentStart">
                            
                            <AllOf>
                                <Match
                                    MatchId="urn:oasis:names:tc:xacml:1.0:function:dateTime-greater-than-or-equal">
                                    <AttributeValue
                                        DataType="http://www.w3.org/2001/XMLSchema#dateTime">
                                        <xsl:value-of select="."/>
                                    </AttributeValue>
                                    <AttributeDesignator MustBePresent="false"
                                        Category="urn:oasis:names:tc:xacml:3.0:attribute-category:environment"
                                        AttributeId="urn:oasis:names:tc:xacml:1.0:environment:current-dateTime"
                                        DataType="http://www.w3.org/2001/XMLSchema#dateTime"/>
                                </Match>
                            </AllOf>
                            
                        </xsl:for-each>
                    </AnyOf>
                    <AnyOf>
                        
                        <xsl:for-each select="//consentEnd">
                            
                            <AllOf>
                                <Match
                                    MatchId="urn:oasis:names:tc:xacml:1.0:function:dateTime-less-than-or-equal">
                                    <AttributeValue
                                        DataType="http://www.w3.org/2001/XMLSchema#dateTime">
                                        <xsl:value-of select="."/>
                                    </AttributeValue>
                                    <AttributeDesignator MustBePresent="false"
                                        Category="urn:oasis:names:tc:xacml:3.0:attribute-category:environment"
                                        AttributeId="urn:oasis:names:tc:xacml:1.0:environment:current-dateTime"
                                        DataType="http://www.w3.org/2001/XMLSchema#dateTime"/>
                                </Match>
                            </AllOf>
                            
                        </xsl:for-each>
                    </AnyOf>
                </Target>
                
                
                
                
                
            </Rule>
            <ObligationExpressions>
                
                <xsl:for-each
                    select="//doNotShareClinicalDocumentSectionTypeCodesList/doNotShareClinicalDocumentSectionTypeCodes">
                    <ObligationExpression
                        ObligationId="urn:samhsa:names:tc:consent2share:1.0:obligation:redact-document-section-code"
                        FulfillOn="Permit">
                        <AttributeAssignmentExpression
                            AttributeId="urn:oasis:names:tc:xacml:3.0:example:attribute:text">
                            <AttributeValue DataType="http://www.w3.org/2001/XMLSchema#string">
                                <xsl:value-of select="code"/>
                            </AttributeValue>
                        </AttributeAssignmentExpression>
                    </ObligationExpression>
                </xsl:for-each>
                
                <xsl:for-each
                    select="//doNotShareSensitivityPolicyCodesList/doNotShareSensitivityPolicyCodes">
                    <ObligationExpression
                        ObligationId="urn:samhsa:names:tc:consent2share:1.0:obligation:redact-sensitivity-code"
                        FulfillOn="Permit">
                        <AttributeAssignmentExpression
                            AttributeId="urn:oasis:names:tc:xacml:3.0:example:attribute:text">
                            <AttributeValue DataType="http://www.w3.org/2001/XMLSchema#string">
                                <xsl:value-of select="code"/>
                            </AttributeValue>
                        </AttributeAssignmentExpression>
                    </ObligationExpression>
                </xsl:for-each>
                
                
                <xsl:for-each
                    select="//doNotShareClinicalConceptCodesList/doNotShareClinicalConceptCodes">
                    <ObligationExpression
                        ObligationId="urn:samhsa:names:tc:consent2share:1.0:obligation:redact-clinical-concept-code"
                        FulfillOn="Permit">
                        <AttributeAssignmentExpression
                            AttributeId="urn:oasis:names:tc:xacml:3.0:example:attribute:text">
                            <AttributeValue DataType="http://www.w3.org/2001/XMLSchema#string">
                                <xsl:value-of select="code"/>
                            </AttributeValue>
                        </AttributeAssignmentExpression>
                    </ObligationExpression>
                </xsl:for-each>
                
            </ObligationExpressions>
        </Policy>
    </xsl:variable>

    <xsl:template match="/">

        <ClinicalDocument xmlns="urn:hl7-org:v3"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" classCode="DOCCLIN" moodCode="EVN">
            <!-- Consent Directive DSTU Header-->
            <realmCode code="US"/>
            <typeId root="2.16.840.1.113883.1.3" extension="09230"/>
            <!-- General Header Constraints -->
            <templateId root="2.16.840.1.113883.10.20.22.1.1"/>
            <!--  Consent Directive Header Constraints -->
            <templateId root="2.16.840.1.113883.3.445.1"/>
            <!-- Document instance id-->
            <id root="1.3.6.4.1.4.1.2835.888888" extension="{$consentId}"/>
            <code code="57016-8" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC"
                displayName="Privacy Policy Acknowledgement Document"/>
            <title representation="TXT" mediaType="text/plain">Privacy Consent Authorization</title>
            <effectiveTime value="{mhc:transformDateTime($currentDateTimeUtc)}"/>
            <confidentialityCode code="N"/>
            <!-- Client/Record Target  Reference-->
            <recordTarget>
                <patientRole>
                    <id extension="{$patient/medicalRecordNumber}" root="2.16.840.1.113883.3.933"/>
                    <addr>
                        <streetAddressLine>
                            <xsl:value-of select="$patient/addressStreetAddressLine"/>
                        </streetAddressLine>
                        <city>
                            <xsl:value-of select="$patient/addressCity"/>
                        </city>
                        <state>
                            <xsl:value-of select="$patient/addressStateCode"/>
                        </state>
                        <postalCode>
                            <xsl:value-of select="$patient/addressPostalCode"/>
                        </postalCode>
                        <country>
                            <xsl:value-of select="$patient/addressCountryCode"/>
                        </country>
                    </addr>
                    <patient>
                        <name>
                            <given>
                                <xsl:value-of select="$patient/firstName"/>
                            </given>
                            <family>
                                <xsl:value-of select="$patient/lastName"/>
                            </family>
                        </name>
                        <administrativeGenderCode code="{$patient/administrativeGenderCode}"
                            codeSystem="2.16.840.1.113883.5.1"/>
                        <birthTime value="{$patient/birthDate}"/>
                    </patient>
                </patientRole>
            </recordTarget>
            <!-- Person and/or organization issuing the consent directive form-->
            <!--<author>
                <templateId root="2.16.840.1.113883.3.445.2"/>
                <functionCode code="POACON"
                    displayName="healthcare power of attorney consent author"
                    codeSystem="2.16.840.1.113883.1.11.19930"
                    codeSystemName="ConsenterParticipationFunction Decision Maker"/>
                <time value="{mhc:transformDateTime($currentDateTimeUtc)}"/>
                <assignedAuthor>
                    <id extension="{$patient/medicalRecordNumber}" root="1.3.5.35.1.4436.7"/>
                    <assignedPerson classCode="PSN">
                        <name>
                            <given>
                                <xsl:value-of select="$patient/firstName"/>
                            </given>
                            <family>
                                <xsl:value-of select="$patient/lastName"/>
                            </family>
                        </name>
                    </assignedPerson>
                    <representedOrganization>
                        <id root="1.3.6.4.1.4.1.2835.2" extension="980983" />
                        <name><xsl:value-of select="concat($patient/firstName,' ', $patient/lastName)"/> </name>
                        <addr>
                            <streetAddressLine>
                                <xsl:value-of select="$patient/addressStreetAddressLine"/>
                            </streetAddressLine>
                            <city>
                                <xsl:value-of select="$patient/addressCity"/>
                            </city>
                            <state>
                                <xsl:value-of select="$patient/addressStateCode"/>
                            </state>
                            <postalCode>
                                <xsl:value-of select="$patient/addressPostalCode"/>
                            </postalCode>
                            <country>
                                <xsl:value-of select="$patient/addressCountryCode"/>
                            </country>
                        </addr>
                    </representedOrganization>
                </assignedAuthor>
            </author>-->
            <author>
                <templateId root="2.16.840.1.113883.3.445.2"/>
                <functionCode code="POACON"
                              displayName="healthcare power of attorney consent author"
                              codeSystem="2.16.840.1.113883.1.11.19930"
                              codeSystemName="ConsenterParticipationFunction Decision Maker"/>
                <time value="{mhc:transformDateTime($currentDateTimeUtc)}"/>
                <assignedAuthor>
                    <id extension="{$patient/medicalRecordNumber}" root="1.3.5.35.1.4436.7"/>

                    <assignedPerson classCode="PSN">
                        <name>
                            <given>
                                <xsl:value-of select="$patient/firstName"/>
                            </given>
                            <family>
                                <xsl:value-of select="$patient/lastName"/>
                            </family>
                        </name>
                    </assignedPerson>
                    <representedOrganization>
                        <id root="1.3.6.4.1.4.1.2835.2" extension="980983" />
                        <name><xsl:value-of select="concat($patient/firstName,' ', $patient/lastName)"/> </name>
                        <addr>
                            <streetAddressLine>
                                <xsl:value-of select="$patient/addressStreetAddressLine"/>
                            </streetAddressLine>
                            <city>
                                <xsl:value-of select="$patient/addressCity"/>
                            </city>
                            <state>
                                <xsl:value-of select="$patient/addressStateCode"/>
                            </state>
                            <postalCode>
                                <xsl:value-of select="$patient/addressPostalCode"/>
                            </postalCode>
                            <country>
                                <xsl:value-of select="$patient/addressCountryCode"/>
                            </country>
                        </addr>
                    </representedOrganization>
                </assignedAuthor>
            </author>

            <xsl:for-each select="//IndividualProvidersPermittedToDiscloseList/IndividualProvidersPermittedToDisclose">
                <custodian>
                    <assignedCustodian>
                        <representedCustodianOrganization>
                            <id root="1.3.6.4.1.4.1.2835.2" extension="npi"/>
                            <name><xsl:value-of select="concat(firstName,' ', lastName)"/></name>
                            <addr>
                                <streetAddressLine>
                                    <xsl:value-of select="firstLinePracticeLocationAddress"/>
                                </streetAddressLine>
                                <city>
                                    <xsl:value-of select="practiceLocationAddressCityName"/>
                                </city>
                                <state>
                                    <xsl:value-of select="practiceLocationAddressStateName"/>
                                </state>
                                <postalCode>
                                    <xsl:value-of select="practiceLocationAddressPostalCode"/>
                                </postalCode>
                            </addr>
                        </representedCustodianOrganization>
                    </assignedCustodian>
                </custodian>

            </xsl:for-each>

            <xsl:for-each
                    select="//OrganizationalProvidersPermittedToDiscloseList/OrganizationalProvidersPermittedToDisclose">

                <custodian>
                    <assignedCustodian>
                        <representedCustodianOrganization>
                            <id root="1.3.6.4.1.4.1.2835.2" extension="npi"/>
                            <name><xsl:value-of select="orgName"/></name>
                            <addr>
                                <streetAddressLine>
                                    <xsl:value-of select="firstLinePracticeLocationAddress"/>
                                </streetAddressLine>
                                <city>
                                    <xsl:value-of select="practiceLocationAddressCityName"/>
                                </city>
                                <state>
                                    <xsl:value-of select="practiceLocationAddressStateName"/>
                                </state>
                                <postalCode>
                                    <xsl:value-of select="practiceLocationAddressPostalCode"/>
                                </postalCode>
                            </addr>
                        </representedCustodianOrganization>
                    </assignedCustodian>
                </custodian>
            </xsl:for-each>
            <!-- Information Recipient - may be both Consent Directive receiver and intended information receiver  -->
            <xsl:for-each select="//IndividualProvidersDisclosureIsMadeToList/IndividualProvidersDisclosureIsMadeTo">
                <informationRecipient typeCode="PRCP">
                    <intendedRecipient classCode="ASSIGNED">
                        <id root="1.3.6.4.1.4.1.2835.2" extension="{npi}"/>
                        <addr>
                            <streetAddressLine>
                                <xsl:value-of select="firstLinePracticeLocationAddress"/>
                            </streetAddressLine>
                            <city>
                                <xsl:value-of select="practiceLocationAddressCityName"/>
                            </city>
                            <state>
                                <xsl:value-of select="practiceLocationAddressStateName"/>
                            </state>
                            <postalCode>
                                <xsl:value-of select="practiceLocationAddressPostalCode"/>
                            </postalCode>
                        </addr>
                        <informationRecipient classCode="PSN" determinerCode="INSTANCE">
                            <name>
                                <prefix>
                                    <xsl:value-of select="namePrefix"/>
                                </prefix>
                                <family>
                                    <xsl:value-of select="lastName"/>
                                </family>
                                <given>
                                    <xsl:value-of select="firstName"/>
                                </given>
                                <suffix>
                                    <xsl:value-of select="nameSuffix"/>
                                </suffix>
                            </name>
                        </informationRecipient>
                    </intendedRecipient>
                </informationRecipient>
            </xsl:for-each>
            <xsl:for-each
                select="//OrganizationalProvidersDisclosureIsMadeToList/OrganizationalProvidersDisclosureIsMadeTo">
                <informationRecipient typeCode="PRCP">
                    <intendedRecipient classCode="ASSIGNED">
                        <id root="1.3.6.4.1.4.1.2835.2" extension="{npi}"/>
                        <addr>
                            <streetAddressLine>
                                <xsl:value-of select="firstLinePracticeLocationAddress"/>
                            </streetAddressLine>
                            <city>
                                <xsl:value-of select="practiceLocationAddressCityName"/>
                            </city>
                            <state>
                                <xsl:value-of select="practiceLocationAddressStateName"/>
                            </state>
                            <postalCode>
                                <xsl:value-of select="practiceLocationAddressPostalCode"/>
                            </postalCode>
                        </addr>
                        <receivedOrganization classCode="ORG" determinerCode="INSTANCE">
                            <id root="1.3.6.4.1.4.1.2835.2" extension="09809"
                                assigningAuthorityName="NPI"/>
                            <name>
                                <xsl:value-of select="orgName"/>
                            </name>
                        </receivedOrganization>
                    </intendedRecipient>
                </informationRecipient>
            </xsl:for-each>



            <!-- Substitute Decision Maker or Patient that signs the Consent Directive -->
            <!--legalAuthenticator contextControlCode="OP" typeCode="LA">
                <time value="20091025"/>
               
                <signatureCode code="S"/>
                <assignedEntity classCode="ASSIGNED">
                    <id extension="11111111" root="1.3.5.35.1.4436.7"/>
                    <assignedPerson classCode="PSN">
                        <name>
                            <given>Bernard</given>
                            <family>Everyperson</family>
                            <suffix>Sr.</suffix>
                        </name>
                    </assignedPerson>
                </assignedEntity>
            </legalAuthenticator-->
            <!-- Witness -->
      <!--      <authenticator>
                <time value="20091025"/>
                <signatureCode code="S"/>
                <assignedEntity>
                    <id extension="112" root="1.3.5.35.1.4436.7"/>
                    <representedOrganization>
                        <name>Adobe Echo Sign</name>
                    </representedOrganization>

                </assignedEntity>
            </authenticator>-->
            <!-- Effective time for the Consent Directive -->
            <documentationOf typeCode="DOC">
                <serviceEvent moodCode="EVN">
                    <templateId root="2.16.840.1.113883.3.445.3"/>
                    <id root="2.16.840.1.113883.3.445.3"/>
                    <code code="57016-8" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC"
                        displayName="Privacy Policy Acknowledgement Document"/>
                    <effectiveTime>
                        <low value="{mhc:transformDateTime($effectivelowUTC)}"/>
                        <high value="{mhc:transformDateTime($effectivehighUTC)}"/>
                    </effectiveTime>
                </serviceEvent>
            </documentationOf>
            <!-- Previous Consent Directive Reference -->
           <!-- <relatedDocument typeCode="RPLC">
                <parentDocument classCode="DOCCLIN" moodCode="EVN">
                    <id root="1.3.6.1.4.1.19376.1.5.3.1.2.6" extension="2345"/>
                </parentDocument>
            </relatedDocument>-->
            <component typeCode="COMP" contextConductionInd="true">
                <structuredBody>
                    <component typeCode="COMP" contextConductionInd="true">
                        <section classCode="DOCSECT" moodCode="EVN">
                            <!-- Consent Directive Details section -->
                            <templateId root="2.16.840.1.113883.3.445.17"/>
                            <code code="57017-6" codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" />
                            <title>Consent Directive Details</title>
                            <!-- Narrative consent  directive-->
                            <text mediaType="text/x-hl7-text+xml">
                                <paragraph>CONSENT FOR RELEASE OF <content
                                        ID="Clinical_Condition_SNOMED-CT_371422002">SUBSTANCE
                                        ABUSE</content> INFORMATION</paragraph>
                                <paragraph> I, <content ID="Patient_Name">
                                        <xsl:value-of
                                            select="concat($patient/firstName,' ', $patient/lastName)"
                                        /></content>
                                    <content ID="permit">authorize</content>
                                    <content ID="CustodianOrganization">
                                        <xsl:for-each
                                            select="//providersPermittedToDiscloseList/providersPermittedToDisclose">
                                            <xsl:value-of select="concat(firstName,' ', lastName)"
                                            />, </xsl:for-each>
                                        <xsl:for-each
                                            select="//organizationalProvidersPermittedToDiscloseList/organizationalProvidersPermittedToDisclose">
                                            <xsl:value-of select="orgName"/>, </xsl:for-each>
                                    </content> to <content ID="Operation">disclose</content> to
                                        <content ID="ReceiverOrganization_1">
                                        <xsl:for-each
                                            select="//providersDisclosureIsMadeToList/providersDisclosureIsMadeTo">
                                            <xsl:value-of select="concat(firstName,' ', lastName)"
                                            />, </xsl:for-each>
                                        <xsl:for-each
                                            select="//organizationalProvidersDisclosureIsMadeToList/organizationalProvidersDisclosureIsMadeTo">
                                            <xsl:value-of select="orgName"/>, </xsl:for-each>
                                    </content> all my medical information except the following
                                    information: </paragraph>

                                <xsl:for-each
                                    select="//doNotShareClinicalDocumentTypeCodesList/doNotShareClinicalDocumentTypeCodes">
                                    <paragraph> * <content><xsl:value-of select="displayName"
                                            /></content></paragraph>
                                </xsl:for-each>

                                <xsl:for-each
                                    select="//doNotShareClinicalDocumentSectionTypeCodesList/doNotShareClinicalDocumentSectionTypeCodes">
                                    <paragraph> * <content><xsl:value-of select="displayName"
                                            /></content></paragraph>
                                </xsl:for-each>

                                <xsl:for-each
                                    select="//doNotShareSensitivityPolicyCodesList/doNotShareSensitivityPolicyCodes">
                                    <paragraph> * <content><xsl:value-of select="displayName"
                                            /></content></paragraph>
                                </xsl:for-each>



                                <paragraph> The purpose of this disclosure is all purpose:
                                        <content ID="purpose">
                                        <xsl:for-each
                                            select="//shareForPurposeOfUseCodesList/shareForPurposeOfUseCodes">
                                            <xsl:value-of select="displayName"/>, </xsl:for-each>
                                    </content></paragraph>
                            </text>
                            <!-- Consent Directive Entry  -->

                                <!-- Structured/computer-readable Consent Directive Specification/Definition -->
                                <xsl:sequence select="$purposeofuse"/>


                            <entry contextConductionInd="true" typeCode="COMP">
                                <act classCode="CONS" moodCode="DEF" negationInd="false">
                                    <code code="DIS" codeSystem="2.16.840.1.113883.1.11.19897"
                                        displayName="Disclose" codeSystemName="ActConsentType"/>
                                    <entryRelationship typeCode="COMP">
                                        <templateId extension="Platform-specific Consent Directive"/>
                                        <observationMedia classCode="OBS" moodCode="EVN">
                                            <value mediaType="text/x-hl7-text+xml" representation="TXT">
                                                
                                                
                                                <xsl:sequence select="$xacml"/>
                                                
                                                
                                            </value>
                                        </observationMedia>
                                    </entryRelationship>
                                </act>
                            </entry>




                        </section>
                    </component>



                </structuredBody>
            </component>
        </ClinicalDocument>





    </xsl:template>



</xsl:stylesheet>
