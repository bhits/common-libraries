package gov.samhsa.c2s.pixclient.service;

import gov.samhsa.c2s.common.marshaller.SimpleMarshaller;
import gov.samhsa.c2s.pixclient.util.PDQSupplierRequestXMLToJava;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.QUQIIN000003UV01Type;
import org.json.XML;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-beans.xml")
@Slf4j
public class PDQSupplierServiceImplTestIT {

    private String Query_REQUEST_XML ="xml/empi_pdqquery_sample.xml";
    private String Continue_REQUEST_XML ="xml/empi_pdqcontinue_sample.xml";
    private String Cancel_REQUEST_XML ="xml/empi_pdqcancel_sample.xml";
    //service under test
    PDQSupplierService pdqSupplierService;

    PDQSupplierRequestXMLToJava requestXMLToJava;
    SimpleMarshaller marshaller;

    @Before
    public void setUp() throws Exception {
        ApplicationContext context=new ClassPathXmlApplicationContext("test-beans.xml");
        pdqSupplierService= (PDQSupplierService) context.getBean("pdqSupplierService");
        requestXMLToJava = (PDQSupplierRequestXMLToJava) context.getBean("pdqSupplierRequestXMLToJava");
    }

    //test for the pdq query
    @Test
    public void testPdqSupplierPRPAIN201305UV02() throws Exception{
        PRPAIN201305UV02 request=new PRPAIN201305UV02();
        PRPAIN201306UV02 response=new PRPAIN201306UV02();

        try{
            request=requestXMLToJava.getPDQQueryReqObject(getRequest(Query_REQUEST_XML));
            response=pdqSupplierService.pdqSupplierPRPAIN201305UV02(request);
            JAXBContext jc=JAXBContext.newInstance(PRPAIN201306UV02.class);
            Marshaller ms=jc.createMarshaller();
            ms.marshal(response,new File("./src/test/resources/xml/pdqquery_result"));
        }catch(Exception e){
            log.error(e.getMessage());
        }
        System.out.println("\n");
        System.out.println("response Acknowledgment code="+response.getAcknowledgement().get(0).getTypeCode().getCode());
        System.out.println("QueryAck="+response.getControlActProcess().getQueryAck().getQueryResponseCode().getCode());
    }

    //test for the pdq continue
    @Test
    public void testPdqSupplierQUQIIN000003UV01Continue() throws  Exception{
        QUQIIN000003UV01Type request=new QUQIIN000003UV01Type();
        PRPAIN201306UV02 response=new PRPAIN201306UV02();

        try {
            request=requestXMLToJava.getPDQContinueReqObject(getRequest(Continue_REQUEST_XML));
            response=pdqSupplierService.pdqSupplierQUQIIN000003UV01Continue(request);

            System.out.println("hello world");
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }

    @Test
    public void testpdqSupplierQUQIIN000003UV01Cancel() throws Exception {
        QUQIIN000003UV01Type request=new QUQIIN000003UV01Type();
        MCCIIN000002UV01 response=new  MCCIIN000002UV01();

        try {
            request=requestXMLToJava.getPDQCancelReqObject(getRequest(Cancel_REQUEST_XML));
            response=pdqSupplierService.pdqSupplierQUQIIN000003UV01Cancel(request);

            System.out.println("hello world");
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }

    @After
    public void cleanup() throws Exception {
        pdqSupplierService = null;
        requestXMLToJava = null;

    }

    private String getRequest(String reqXml) {
        InputStream ioStream = ClassLoader.getSystemResourceAsStream(reqXml);
        String sampleReq = null;
        try {
            sampleReq = IOUtils.toString(ioStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error(e.getMessage() + e);
        }
        return sampleReq;
    }

}

