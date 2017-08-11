package gov.samhsa.c2s.pixclient.util;

import lombok.extern.slf4j.Slf4j;

import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201302UV02;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@Slf4j
public class PixManagerRequestXMLToJavaTest {

    JAXBContext context = null;

    @Before
    public void setUp() throws Exception {
        try {
            // Create JAXContext instance
            context = JAXBContext.newInstance("org.hl7.v3");
        } catch (JAXBException e) {
            log.error(e.getMessage() + e);
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void pixAddMarshalUnMarshalSuccess() throws Exception {
        String inputFile = "xml/iexhub_pixadd.xml";
        StringWriter sw = new StringWriter();
        PRPAIN201301UV02 prpain201301UV02 = new PRPAIN201301UV02();
        try {

            //Use JAXBContext instance to create the Unmarshaller.
            Unmarshaller unmarshaller = context.createUnmarshaller();
            //Use the Unmarshaller to unmarshal the XML document to get an instance of JAXBElement.
            InputStream ioStream = ClassLoader.getSystemResourceAsStream(inputFile);
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
        String inputFile = "xml/04_PatientRegistryRecordRevised2.xml";
        StringWriter sw = new StringWriter();
        PRPAIN201302UV02 prpain201302UV02 = new PRPAIN201302UV02();
        try {
            //Use JAXBContext instance to create the Unmarshaller.
            Unmarshaller unmarshaller = context.createUnmarshaller();
            //Use the Unmarshaller to unmarshal the XML document to get an instance of JAXBElement.
            InputStream ioStream = ClassLoader.getSystemResourceAsStream(inputFile);
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