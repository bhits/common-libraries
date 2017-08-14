package gov.samhsa.c2s.pixclient.util;

import lombok.extern.slf4j.Slf4j;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201302UV02;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.StringWriter;

import static org.junit.Assert.assertNotNull;

@Slf4j
public class PixManagerRequestXMLToJavaTest {

    private String ADD_REQUEST_XML = "xml/iexhub_pixadd.xml";
    private String UPDATE_REQUEST_XML = "xml/04_PatientRegistryRecordRevised2.xml";
    private String PACKAGE_NAME = "org.hl7.v3";

    private JAXBContext context = null;

    @Before
    public void setUp() throws Exception {
        try {
            // Create JAXContext instance
            context = JAXBContext.newInstance(PACKAGE_NAME);
        } catch (JAXBException e) {
            log.error(e.getMessage() + e);
        }
    }

    @Test
    public void pixAddMarshalUnMarshalSuccess() throws Exception {
        StringWriter sw = new StringWriter();
        PRPAIN201301UV02 prpain201301UV02;
        try {

            //Use JAXBContext instance to create the Unmarshaller.
            Unmarshaller unmarshaller = context.createUnmarshaller();
            //Use the Unmarshaller to unmarshal the XML document to get an instance of JAXBElement.
            InputStream ioStream = ClassLoader.getSystemResourceAsStream(ADD_REQUEST_XML);
            assertNotNull(ioStream);

            prpain201301UV02 = (PRPAIN201301UV02) unmarshaller.unmarshal(ioStream);
            assertNotNull(prpain201301UV02);

            //Get the instance of the required JAXB Root Class from the JAXBElement.
            Marshaller marshaller = context.createMarshaller();

            //output pretty printed
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(prpain201301UV02, sw);
            log.debug("Add Marshal String" + sw.toString());

        } catch (JAXBException e) {
            log.error("exception on Marshal/UnMMarshal" + e);
        }
    }

    @Test
    public void pixUpdateMarshalUnMarshalSuccess() throws Exception {
        StringWriter sw = new StringWriter();
        PRPAIN201302UV02 prpain201302UV02;
        try {
            //Use JAXBContext instance to create the Unmarshaller.
            Unmarshaller unmarshaller = context.createUnmarshaller();
            //Use the Unmarshaller to unmarshal the XML document to get an instance of JAXBElement.
            InputStream ioStream = ClassLoader.getSystemResourceAsStream(UPDATE_REQUEST_XML);
            assertNotNull(ioStream);
            prpain201302UV02 = (PRPAIN201302UV02) unmarshaller.unmarshal(ioStream);

            assertNotNull(prpain201302UV02);
            Marshaller marshaller = context.createMarshaller();

            //output pretty printed
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(prpain201302UV02, sw);
            log.debug("Update Marshal String" + sw.toString());

        } catch (JAXBException e) {
            log.error("exception on Marshal/UnMMarshal" + e);
        }
    }
}