package gov.samhsa.c2s.pixclient.service;

import lombok.extern.slf4j.Slf4j;
import org.hl7.v3.MCCIIN000002UV01;
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
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-beans.xml")
@Slf4j
public class PDQSupplierServiceImplTestIT {
    PDQSupplierService pdqSupplierService;

    private String Query_REQUEST_XML = "./src/test/resources/xml/empi_pdqquery_sample.xml";
    private String Query_Continue_Cancel_REQUEST_XML = "./src/test/resources/xml/empi_pdqquery_continue_cancel_sample.xml";
    private String Continue_REQUEST_XML = "./src/test/resources/xml/empi_pdqcontinue_sample.xml";
    private String Cancel_REQUEST_XML = "./src/test/resources/xml/empi_pdqcancel_sample.xml";

    @Before
    public void setUp() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("test-beans.xml");
        pdqSupplierService = (PDQSupplierService) context.getBean("pdqSupplierService");
    }

    @Test
    public void testPdqQuery() throws Exception {
        PRPAIN201305UV02 request = getFile(PRPAIN201305UV02.class, Query_REQUEST_XML);
        PRPAIN201306UV02 response = pdqSupplierService.pdqQuery(request);

        log.info("Query Response " + response.getControlActProcess().getQueryAck().getQueryResponseCode().getCode());
        log.info("Total Results Quantinty "+response.getControlActProcess().getQueryAck().getResultTotalQuantity().getValue());
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

