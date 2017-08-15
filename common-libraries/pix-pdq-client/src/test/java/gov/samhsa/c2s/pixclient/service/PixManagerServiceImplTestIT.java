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
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAIN201310UV02;
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
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-beans.xml")
@Slf4j
public class PixManagerServiceImplTestIT {

    private String ADD_REQUEST_XML = "xml/empi_pixadd_sample.xml";
    private String UPDATE_REQUEST_XML = "xml/empi_pixupdate_sample.xml";
    private String QUERY_REQUEST_XML = "xml/empi_pixquery_sample.xml";
    private String PIX_MGR_SERVICE_BEAN_NAME = "pixManagerService";
    private String PIX_MGR_REQUEST_XML_TO_JAVA_BEAN_NAME = "pixManagerRequestXMLToJava";
    private String PIX_MGR_MSG_HELPER_BEAN_NAME = "pixManagerMessageHelper";
    private String TEST_BEAN_FILE = "test-beans.xml";
    private String GLOBAL_DOMAIN_ID = "2.16.840.1.113883.4.357";

    private PixManagerService pixManagerService;
    private PixManagerRequestXMLToJava requestXMLToJava;
    private PixManagerMessageHelper pixManagerMessageHelper;

    private String getRequest(String reqXml) {
        String sampleReq = null;
        try (InputStream ioStream = ClassLoader.getSystemResourceAsStream(reqXml)) {
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
            request = requestXMLToJava.getPIXAddReqObject(getRequest(ADD_REQUEST_XML));
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
            request = requestXMLToJava.getPIXUpdateReqObject(getRequest(UPDATE_REQUEST_XML));
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

    @Test
    public void queryPatientRecord() throws Exception {
        PRPAIN201309UV02 request;
        PRPAIN201310UV02 response;

        PixManagerBean pixManagerBean = new PixManagerBean();
        // Delegate to webServiceTemplate for the actual pixadd
        try {
            request = requestXMLToJava.getPIXQueryReqObject(getRequest(QUERY_REQUEST_XML));
            response = pixManagerService.pixManagerPRPAIN201309UV02(request);
            pixManagerMessageHelper.getQueryMessage(response, pixManagerBean);
        } catch (JAXBException | IOException e) {
            pixManagerMessageHelper.getGeneralExpMessage(e, pixManagerBean,
                    PixPdqConstants.PIX_QUERY.getMsg());
            log.error(e.getMessage());
        }
        log.debug("response" + pixManagerBean.getQueryMessage() + pixManagerBean.getQueryIdMap());
        String eid = pixManagerBean.getQueryIdMap().entrySet().stream()
                .filter(map -> GLOBAL_DOMAIN_ID.equals(map.getKey()))
                .map(map -> map.getValue())
                .collect(Collectors.joining());
        log.info("Eid \t" + eid);
    }

    @After
    public void cleanup() throws Exception {
        pixManagerService = null;
        requestXMLToJava = null;
        pixManagerMessageHelper = null;
    }

}