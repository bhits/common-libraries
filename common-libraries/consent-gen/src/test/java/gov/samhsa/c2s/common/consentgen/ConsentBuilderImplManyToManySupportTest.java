package gov.samhsa.c2s.common.consentgen;

import gov.samhsa.c2s.common.consentgen.pg.XacmlXslUrlProviderImpl;
import gov.samhsa.c2s.common.document.accessor.DocumentAccessor;
import gov.samhsa.c2s.common.document.accessor.DocumentAccessorImpl;
import gov.samhsa.c2s.common.document.converter.DocumentXmlConverter;
import gov.samhsa.c2s.common.document.converter.DocumentXmlConverterImpl;
import gov.samhsa.c2s.common.document.transformer.XmlTransformerImpl;
import gov.samhsa.c2s.common.marshaller.SimpleMarshallerImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ConsentBuilderImplManyToManySupportTest {
    private static final String PCM_ORG = "urn:oid:1.3.6.1.4.1.21367.13.20.200";

    private static final String XACML2_POLICY_POLICY_ID = "/xacml2:Policy/@PolicyId";
    private static final String XACML2_ATTRIBUTE_VALUE_XPATH_TEMPLATE = "/xacml2:Policy/xacml2:Rule/xacml2:Condition//xacml2:Apply[xacml2:SubjectAttributeDesignator[@AttributeId='%1']]/following-sibling::xacml2:AttributeValue/text()";
    private static final String XACML2_OBLIGATION_ATTRIBUTE_ASSIGNMENT_XPATH_TEMPLATE = "/xacml2:Policy/xacml2:Obligations/xacml2:Obligation[@ObligationId='urn:samhsa:names:tc:consent2share:1.0:obligation:share-sensitivity-policy-code']/xacml2:AttributeAssignment/text()";

    private static final String FROM_ATTRIBUTE_ID = "urn:oasis:names:tc:xacml:1.0:subject-category:intermediary-subject";
    private static final String TO_ATTRIBUTE_ID = "urn:oasis:names:tc:xacml:1.0:subject-category:recipient-subject";
    private static final String PURPOSE_OF_USE_ATTRIBUTE_ID = "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse";

    // real instances
    private XacmlXslUrlProviderImpl xacmlXslUrlProvider;
    private MockConsentDtoFactory consentDtoFactory;
    private SimpleMarshallerImpl marshaller;
    private XmlTransformerImpl xmlTransformer;

    // utils
    private DocumentAccessor documentAccessor;
    private DocumentXmlConverter documentXmlConverter;

    private ConsentBuilderImpl sut;

    @Before
    public void setUp() throws Exception {
        // utils
        documentAccessor = new DocumentAccessorImpl();
        documentXmlConverter = new DocumentXmlConverterImpl();
        // sut init
        xacmlXslUrlProvider = new XacmlXslUrlProviderImpl();
        consentDtoFactory = new MockConsentDtoFactory();
        marshaller = new SimpleMarshallerImpl();
        xmlTransformer = new XmlTransformerImpl(marshaller);
        sut = new ConsentBuilderImpl(PCM_ORG, xacmlXslUrlProvider, consentDtoFactory, xmlTransformer);
    }

    @Test
    public void buildConsent2Xacml_Many_To_Many_Support() throws Exception {
        // Act
        final String s = sut.buildConsent2Xacml(MockConsentDtoFactory.MOCK_REQUEST_OBJECT);

        // Assert
        final Document document = documentXmlConverter.loadDocument(s);
        final String consentReferenceId = documentAccessor.getNode(document, XACML2_POLICY_POLICY_ID).map(Node::getNodeValue).get();
        final Set<String> intermediaryNpis = documentAccessor.getNodeListAsStream(document, XACML2_ATTRIBUTE_VALUE_XPATH_TEMPLATE, FROM_ATTRIBUTE_ID).map(Node::getNodeValue).collect(toSet());
        final Set<String> recipientNpis = documentAccessor.getNodeListAsStream(document, XACML2_ATTRIBUTE_VALUE_XPATH_TEMPLATE, TO_ATTRIBUTE_ID).map(Node::getNodeValue).collect(toSet());
        final Set<String> purposes = documentAccessor.getNodeListAsStream(document, XACML2_ATTRIBUTE_VALUE_XPATH_TEMPLATE, PURPOSE_OF_USE_ATTRIBUTE_ID).map(Node::getNodeValue).collect(toSet());
        final Set<String> obligations = documentAccessor.getNodeListAsStream(document, XACML2_OBLIGATION_ATTRIBUTE_ASSIGNMENT_XPATH_TEMPLATE).map(Node::getNodeValue).collect(toSet());

        assertEquals("consent reference id doesn't match", MockConsentDtoFactory.MOCK_CONSENT_REFERENCE_ID, consentReferenceId);
        assertTrue("extra intermediary NPIs that are not supposed to exist", MockConsentDtoFactory.INTERMEDIARY_NPIS.containsAll(intermediaryNpis));
        assertTrue("missing intermediary NPIs", intermediaryNpis.containsAll(MockConsentDtoFactory.INTERMEDIARY_NPIS));
        assertTrue("extra recipient NPIs that are not supposed to exist", MockConsentDtoFactory.RECIPIENT_NPIS.containsAll(recipientNpis));
        assertTrue("missing recipient NPIs", recipientNpis.containsAll(MockConsentDtoFactory.RECIPIENT_NPIS));
        assertTrue("extra purpose of use codes that are not supposed to exist", MockConsentDtoFactory.PURPOSES.containsAll(purposes));
        assertTrue("missing purpose of use codes", purposes.containsAll(MockConsentDtoFactory.PURPOSES));
        assertTrue("extra obligations that are not supposed to exist", MockConsentDtoFactory.OBLIGATIONS.containsAll(obligations));
        assertTrue("missing obligations", obligations.containsAll(MockConsentDtoFactory.OBLIGATIONS));
    }
}