package gov.samhsa.c2s.pixclient.service;

import gov.samhsa.c2s.pixclient.util.PixManagerBean;
import gov.samhsa.c2s.pixclient.util.PixManagerMessageHelper;
import gov.samhsa.c2s.pixclient.util.PixManagerRequestXMLToJava;
import gov.samhsa.c2s.pixclient.util.PixPdqConstants;
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
    
    private String ADD_REQUEST_XML ="xml/iexhub_pixadd.xml";
    private String UPDATE_REQUEST_XML ="xml/04_PatientRegistryRecordRevised2.xml";
    private String PIX_MGR_SERVICE_BEAN_NAME ="pixManagerService";
    private String PIX_MGR_REQUEST_XML_TO_JAVA_BEAN_NAME ="pixManagerRequestXMLToJava";
    private String PIX_MGR_MSG_HELPER_BEAN_NAME ="pixManagerMessageHelper";
    private String TEST_BEAN_FILE ="test-beans.xml";

    private PixManagerService pixManagerService;
    private PixManagerRequestXMLToJava requestXMLToJava;
    private PixManagerMessageHelper pixManagerMessageHelper;

    private String addRequest() {
        InputStream ioStream = ClassLoader.getSystemResourceAsStream(ADD_REQUEST_XML);
        String sampleReq = null;
        try {
            sampleReq = IOUtils.toString(ioStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error(e.getMessage() + e);
        }
        return sampleReq;
    }

    private String updateRequest() {
        InputStream ioStream = ClassLoader.getSystemResourceAsStream(UPDATE_REQUEST_XML);
        String sampleReq = null;
        try {
            sampleReq = IOUtils.toString(ioStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error(e.getMessage() + e);
        }
        return sampleReq;
    }

    @Before
    public void setUp() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                TEST_BEAN_FILE);
        pixManagerService = (PixManagerService) context.getBean(PIX_MGR_SERVICE_BEAN_NAME);
        requestXMLToJava = (PixManagerRequestXMLToJava) context.getBean(PIX_MGR_REQUEST_XML_TO_JAVA_BEAN_NAME);
        pixManagerMessageHelper = (PixManagerMessageHelper) context.getBean(PIX_MGR_MSG_HELPER_BEAN_NAME);
    }

    @Test
    public void addPatientRecord() throws Exception {
        PRPAIN201301UV02 request;

        MCCIIN000002UV01 response;

        PixManagerBean pixManagerBean = new PixManagerBean();
        // Delegate to webServiceTemplate for the actual pixadd
        try {
            request = requestXMLToJava.getPIXAddReqObject(addRequest());
            response = pixManagerService.pixManagerPRPAIN201301UV02(request);
            pixManagerMessageHelper.getAddUpdateMessage(response, pixManagerBean,
                    PixPdqConstants.PIX_ADD.getMsg());
        } catch (JAXBException | IOException e) {
            pixManagerMessageHelper.getGeneralExpMessage(e, pixManagerBean,
                    PixPdqConstants.PIX_ADD.getMsg());
            log.error(e.getMessage() + e);
        }
        log.debug("response" + pixManagerBean.getAddMessage());


    }

    @Test
    public void udpatePatientRecord() throws Exception {
        PRPAIN201302UV02 request;
        MCCIIN000002UV01 response;

        PixManagerBean pixManagerBean = new PixManagerBean();
        // Delegate to webServiceTemplate for the actual pixadd
        try {
            request = requestXMLToJava.getPIXUpdateReqObject(updateRequest());
            response = pixManagerService.pixManagerPRPAIN201302UV02(request);
            pixManagerMessageHelper.getAddUpdateMessage(response, pixManagerBean,
                    PixPdqConstants.PIX_UPDATE.getMsg());
        } catch (JAXBException | IOException e) {
            pixManagerMessageHelper.getGeneralExpMessage(e, pixManagerBean,
                    PixPdqConstants.PIX_UPDATE.getMsg());
            log.error(e.getMessage());
        }
        log.debug("response" + pixManagerBean.getAddMessage());
    }

    @After
    public void cleanup() throws Exception {
        pixManagerService = null;
        requestXMLToJava = null;
        pixManagerMessageHelper = null;
    }

}