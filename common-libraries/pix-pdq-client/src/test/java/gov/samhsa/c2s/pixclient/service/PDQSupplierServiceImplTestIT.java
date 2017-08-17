package gov.samhsa.c2s.pixclient.service;

import lombok.extern.slf4j.Slf4j;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.QUQIIN000003UV01Type;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-beans.xml")
@Slf4j
public class PDQSupplierServiceImplTestIT {
    PDQSupplierService pdqSupplierService;
    PixManagerService pixManagerService;

    private String TEST_BEANS_FILE = "test-beans.xml";
    private String PIX_MGR_SERVICE_BEAN_NAME = "pixManagerService";
    private String PDQ_SUP_SERVICE_BEAN_NAME = "pdqSupplierService";

    private String ADD_REQUEST_XML1 = "./src/test/resources/xml/pdq_pixadd_sample.xml";
    private String ADD_REQUEST_XML2 = "./src/test/resources/xml/pdq_pixadd_sample2.xml";
    private String Query_REQUEST_XML = "./src/test/resources/xml/empi_pdqquery_sample.xml";
    private String Continue_REQUEST_XML = "./src/test/resources/xml/empi_pdqcontinue_sample.xml";
    private String Cancel_REQUEST_XML = "./src/test/resources/xml/empi_pdqcancel_sample.xml";

    @Before
    public void setUp() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext(TEST_BEANS_FILE);
        pixManagerService = (PixManagerService) context.getBean(PIX_MGR_SERVICE_BEAN_NAME);
        pdqSupplierService = (PDQSupplierService) context.getBean(PDQ_SUP_SERVICE_BEAN_NAME);
    }

    @Test
    public void testPdqQuery() throws Exception {
        //Arrange
        //Add the sample data from PIXAdd
        PRPAIN201301UV02 addRequest1 = getFile(PRPAIN201301UV02.class, ADD_REQUEST_XML1);
        pixManagerService.pixManagerPRPAIN201301UV02(addRequest1);

        PRPAIN201301UV02 addRequest2 = getFile(PRPAIN201301UV02.class, ADD_REQUEST_XML2);
        pixManagerService.pixManagerPRPAIN201301UV02(addRequest2);

        PRPAIN201305UV02 request = getFile(PRPAIN201305UV02.class, Query_REQUEST_XML);
        PRPAIN201306UV02 response = pdqSupplierService.pdqQuery(request);

        //Act and Assert
        assertEquals(BigInteger.valueOf(2), response.getControlActProcess().getQueryAck().getResultTotalQuantity().getValue());

        log.info("Query Response " + response.getControlActProcess().getQueryAck().getQueryResponseCode().getCode());
        log.info("Total Results Quantinty " + response.getControlActProcess().getQueryAck().getResultTotalQuantity().getValue());
    }

    @Test
    public void testPdqQueryContinue() throws Exception {
        //Arrange
        //Add the sample data from PIXAdd
        PRPAIN201301UV02 addRequest1 = getFile(PRPAIN201301UV02.class, ADD_REQUEST_XML1);
        pixManagerService.pixManagerPRPAIN201301UV02(addRequest1);

        PRPAIN201301UV02 addRequest2 = getFile(PRPAIN201301UV02.class, ADD_REQUEST_XML2);
        pixManagerService.pixManagerPRPAIN201301UV02(addRequest2);

        //Query Request
        PRPAIN201305UV02 request0 = getFile(PRPAIN201305UV02.class, Query_REQUEST_XML);
        pdqSupplierService.pdqQuery(request0);

        //Continue Request
        QUQIIN000003UV01Type request = getFile(QUQIIN000003UV01Type.class, Continue_REQUEST_XML);
        PRPAIN201306UV02 response = pdqSupplierService.pdqQueryContinue(request);

        //Act and Assert
        assertEquals(BigInteger.valueOf(1), response.getControlActProcess().getQueryAck().getResultCurrentQuantity().getValue());

        log.info("Query Response " + response.getControlActProcess().getQueryAck().getQueryResponseCode().getCode());
        log.info("Total Results Quantity " + response.getControlActProcess().getQueryAck().getResultTotalQuantity().getValue());
        log.info("Initial Quantity " + response.getControlActProcess().getQueryByParameter().getValue().getInitialQuantity().getValue());
        log.info("Total Current Quantity " + response.getControlActProcess().getQueryAck().getResultCurrentQuantity().getValue());
        log.info("Remaining Quantity " + response.getControlActProcess().getQueryAck().getResultRemainingQuantity().getValue());
    }

    @Test
    public void testpdqQueryCancel() throws Exception {
        //Arrange
        //Add the sample data from PIXAdd
        PRPAIN201301UV02 addRequest1 = getFile(PRPAIN201301UV02.class, ADD_REQUEST_XML1);
        pixManagerService.pixManagerPRPAIN201301UV02(addRequest1);

        PRPAIN201301UV02 addRequest2 = getFile(PRPAIN201301UV02.class, ADD_REQUEST_XML2);
        pixManagerService.pixManagerPRPAIN201301UV02(addRequest2);

        //Query Request
        PRPAIN201305UV02 request0 = getFile(PRPAIN201305UV02.class, Query_REQUEST_XML);
        pdqSupplierService.pdqQuery(request0);

        //Cancel Request
        QUQIIN000003UV01Type request = getFile(QUQIIN000003UV01Type.class, Cancel_REQUEST_XML);
        MCCIIN000002UV01 response = pdqSupplierService.pdqQueryCancel(request);

        //Act and assert
        assertEquals("CA", response.getAcknowledgement().get(0).getTypeCode().getCode());
    }

    @After
    public void cleanup() throws Exception {
        pdqSupplierService = null;
    }

    private <T> T getFile(Class<T> clazz, String reqXmlFilePath) {
        File file = new File(reqXmlFilePath);
        JAXBContext jaxbContext = null;
        T input = null;
        try {
            jaxbContext = JAXBContext.newInstance(clazz);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Source source = new StreamSource(file);
            JAXBElement<T> root = jaxbUnmarshaller.unmarshal(source, clazz);
            input = root.getValue();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return input;
    }

}

