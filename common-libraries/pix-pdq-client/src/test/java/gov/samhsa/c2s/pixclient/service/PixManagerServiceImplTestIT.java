package gov.samhsa.c2s.pixclient.service;

import gov.samhsa.c2s.pixclient.util.PixManagerBean;
import gov.samhsa.c2s.pixclient.util.PixManagerConstants;
import gov.samhsa.c2s.pixclient.util.PixManagerMessageHelper;
import gov.samhsa.c2s.pixclient.util.PixManagerRequestXMLToJava;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201302UV02;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-beans.xml")
@Slf4j
public class PixManagerServiceImplTestIT {
    PixManagerService pixManagerService;
    PixManagerRequestXMLToJava requestXMLToJava;
    PixManagerMessageHelper pixManagerMessageHelper;

    private static InputStream addRequestIs() {
        return ClassLoader.getSystemResourceAsStream("xml/iexhub_pixadd.xml");
    }

    private static String addRequest() {
        InputStream ioStream = ClassLoader.getSystemResourceAsStream("xml/iexhub_pixadd.xml");
        String sampleReq = null;
        try {
            sampleReq = IOUtils.toString(ioStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sampleReq;
    }

    private static String updateRequest() {
        InputStream ioStream = ClassLoader.getSystemResourceAsStream("xml/04_PatientRegistryRecordRevised2.xml");
        String sampleReq = null;
        try {
            sampleReq = IOUtils.toString(ioStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sampleReq;
    }

    @Before
    public void setUp() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "test-beans.xml");
        pixManagerService = (PixManagerService) context.getBean("pixManagerService");
        requestXMLToJava = (PixManagerRequestXMLToJava) context.getBean("pixManagerRequestXMLToJava");
        pixManagerMessageHelper = (PixManagerMessageHelper) context.getBean("pixManagerMessageHelper");
    }

    @Test
    public void addPatientRecord() throws Exception {
        PRPAIN201301UV02 request = new PRPAIN201301UV02();

        MCCIIN000002UV01 response = new MCCIIN000002UV01();

        PixManagerBean pixManagerBean = new PixManagerBean();
        // Delegate to webServiceTemplate for the actual pixadd
        try {
            request = requestXMLToJava.getPIXAddReqObject(addRequest(),
                    PixManagerConstants.ENCODE_STRING);
            response = pixManagerService.pixManagerPRPAIN201301UV02(request);
            pixManagerMessageHelper.getAddUpdateMessage(response, pixManagerBean,
                    PixManagerConstants.PIX_ADD);
        } catch (JAXBException | IOException e) {
            pixManagerMessageHelper.getGeneralExpMessage(e, pixManagerBean,
                    PixManagerConstants.PIX_ADD);
            log.error(e.getMessage());
        }
        System.out.println("response" + pixManagerBean.getAddMessage());


    }

    @Test
    public void udpatePatientRecord() throws Exception {
        PRPAIN201302UV02 request = new PRPAIN201302UV02();
        MCCIIN000002UV01 response = new MCCIIN000002UV01();

        PixManagerBean pixManagerBean = new PixManagerBean();
        // Delegate to webServiceTemplate for the actual pixadd
        try {
            request = requestXMLToJava.getPIXUpdateReqObject(updateRequest(),
                    PixManagerConstants.ENCODE_STRING);
            response = pixManagerService.pixManagerPRPAIN201302UV02(request);
            pixManagerMessageHelper.getAddUpdateMessage(response, pixManagerBean,
                    PixManagerConstants.PIX_UPDATE);
        } catch (JAXBException | IOException e) {
            pixManagerMessageHelper.getGeneralExpMessage(e, pixManagerBean,
                    PixManagerConstants.PIX_UPDATE);
            log.error(e.getMessage());
        }
        System.out.println("response" + pixManagerBean.getAddMessage());
    }

    @After
    public void cleanup() throws Exception {
        pixManagerService = null;
        requestXMLToJava = null;
        pixManagerMessageHelper = null;

    }

}