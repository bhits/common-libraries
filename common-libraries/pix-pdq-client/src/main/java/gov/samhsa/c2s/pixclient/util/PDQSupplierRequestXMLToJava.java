package gov.samhsa.c2s.pixclient.util;

import gov.samhsa.c2s.common.marshaller.SimpleMarshaller;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201302UV02;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.QUQIIN000003UV01Type;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@Slf4j
public class PDQSupplierRequestXMLToJava {
    /**
     * The marshaller.
     */
    private SimpleMarshaller marshaller;

    /**
     * Instantiates a new pix manager request xml to java.
     */
    public PDQSupplierRequestXMLToJava() {
    }

    /**
     * Instantiates a new pix manager request xml to java.
     *
     * @param marshaller the marshaller
     */
    public PDQSupplierRequestXMLToJava(SimpleMarshaller marshaller) {
        this.marshaller = marshaller;
    }

    /**
     * Gets the pdq query req object.
     *
     * @param reqXMLFilePath the req xml file path
     * @return the pdq query req object
     * @throws JAXBException the jAXB exception
     * @throws IOException   Signals that an I/O exception has occurred.
     */
    public PRPAIN201305UV02 getPDQQueryReqObject(String reqXMLFilePath) throws JAXBException, IOException {
        return getPDQReqObject(PRPAIN201305UV02.class, reqXMLFilePath);
    }

    /**
     * Gets the pdq continue req object.
     *
     * @param reqXMLFilePath the req xml file path
     * @throws JAXBException the jAXB exception
     * @throws IOException   Signals that an I/O exception has occurred.
     */

    public QUQIIN000003UV01Type getPDQContinueReqObject(String reqXMLFilePath) throws JAXBException, IOException {
        return getPDQReqObject(QUQIIN000003UV01Type.class, reqXMLFilePath);

    }

    /**
     * Gets the pIX query req object.
     *
     * @param reqXMLFilePath the req xml file path
     * @throws JAXBException the jAXB exception
     * @throws IOException   Signals that an I/O exception has occurred.
     */
    public QUQIIN000003UV01Type getPDQCancelReqObject(String reqXMLFilePath) throws JAXBException, IOException {
        return getPDQReqObject(QUQIIN000003UV01Type.class, reqXMLFilePath);
    }

    /**
     * Gets the pIX req object.
     *
     * @param <T>            the generic type
     * @param clazz          the clazz
     * @param reqXMLFilePath the req xml file path
     * @return the pIX req object
     * @throws JAXBException the jAXB exception
     * @throws IOException   Signals that an I/O exception has occurred
     */
    private <T> T getPDQReqObject(Class<T> clazz, String reqXMLFilePath)
            throws JAXBException, IOException {
        T reqObj = null;
        if (reqXMLFilePath == null) {
            throw new JAXBException("input is null");
        }

        // if the string starts with <?xml then its a xml document
        // otherwise its xml file path
        if (reqXMLFilePath.startsWith("<?xml")) {
            // 3. Use the Unmarshaller to unmarshal the XML document to get an
            // instance of JAXBElement.
            // 4. Get the instance of the required JAXB Root Class from the
            // JAXBElement.
            try {
                reqObj = marshaller.unmarshalFromXml(clazz, reqXMLFilePath);
            } catch (JAXBException e) {
                log.error(e.getMessage(), e);
                throw e;
            }
        } else {
            // 3. Use the Unmarshaller to unmarshal the XML document to get an
            // instance of JAXBElement.
            // 4. Get the instance of the required JAXB Root Class from the
            // JAXBElement.
            try {
                reqObj = marshaller.unmarshalFromXml(clazz, IOUtils
                        .toString(getClass().getClassLoader()
                                .getResourceAsStream(reqXMLFilePath)));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw e;
            } catch (JAXBException e) {
                log.error(e.getMessage(), e);
                throw e;
            }
        }
        return reqObj;
    }

}
