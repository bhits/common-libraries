package gov.samhsa.c2s.pixclient.service;

import gov.samhsa.c2s.common.cxf.AbstractEnhancedCxfClient;
import gov.samhsa.c2s.common.cxf.SoapVersion;
import gov.samhsa.c2s.pixclient.service.exception.PixManagerServiceException;
import lombok.extern.slf4j.Slf4j;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201302UV02;
import org.hl7.v3.PRPAIN201304UV02;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAIN201310UV02;
import org.openhie.openpixpdq.services.PIXManagerService;
import org.springframework.util.Assert;

import javax.xml.namespace.QName;
import java.net.URL;

/**
 * The Class PixManagerServiceImpl.
 */
@Slf4j
public class PixManagerServiceImpl extends AbstractEnhancedCxfClient implements PixManagerService {

    /**
     * The service name.
     */
    private static final QName SERVICE_NAME = new QName("urn:ihe:iti:pixv3:2007",
            "PIXManager_Service");
    /**
     * The wsdl url.
     */
    final URL wsdlURL = this.getClass().getClassLoader()
            .getResource("wsdl/PIXManager.wsdl");

    /**
     * Instantiates a new pix manager service impl.
     *
     * @param endpointAddress the endpoint address
     */
    public PixManagerServiceImpl(String endpointAddress, SoapVersion soapVersion) {
        super(SERVICE_NAME, endpointAddress);
        Assert.hasText(endpointAddress, "'endpointAddress' must have text");
        Assert.notNull(soapVersion, "'soapVersion' must not be null");
        setSoapVersion(soapVersion);
    }

    @Override
    protected Class<PIXManagerService.PIXManagerPortTypeProxy> getPortTypeClass() {
        return PIXManagerService.PIXManagerPortTypeProxy.class;
    }

    /**
     * Pix manager PRPAIN201301UV02 (Add).
     *
     * @param body the body
     * @return the MCCIIN000002UV01 (Acknowledgement)
     */
    @Override
    public MCCIIN000002UV01 pixManagerPRPAIN201301UV02(PRPAIN201301UV02 body) {
        try (PIXManagerService.PIXManagerPortTypeProxy port = createPort()) {
            final MCCIIN000002UV01 pixManagerPRPAIN201301UV02 = port
                    .pixManagerPRPAIN201301UV02(body);
            return pixManagerPRPAIN201301UV02;
        } catch (final Exception e) {
            throw toPixManagerServiceException(e);
        }
    }

    /**
     * Pix manager PRPAIN201302UV02 (Update).
     *
     * @param body the body
     * @return the MCCIIN000002UV01 (Acknowledgement)
     */
    @Override
    public MCCIIN000002UV01 pixManagerPRPAIN201302UV02(PRPAIN201302UV02 body) {
        try (PIXManagerService.PIXManagerPortTypeProxy port = createPort()) {
            final MCCIIN000002UV01 pixManagerPRPAIN201302UV02 = port
                    .pixManagerPRPAIN201302UV02(body);
            return pixManagerPRPAIN201302UV02;
        } catch (final Exception e) {
            throw toPixManagerServiceException(e);
        }
    }

    /**
     * Pix manager PRPAIN201304UV02 (Merge).
     *
     * @param body the body
     * @return the MCCIIN000002UV01 (Acknowledgement)
     */
    @Override
    public MCCIIN000002UV01 pixManagerPRPAIN201304UV02(PRPAIN201304UV02 body) {
        try (PIXManagerService.PIXManagerPortTypeProxy port = createPort()) {
            final MCCIIN000002UV01 pixManagerPRPAIN201304UV02 = port
                    .pixManagerPRPAIN201304UV02(body);
            return pixManagerPRPAIN201304UV02;
        } catch (final Exception e) {
            throw toPixManagerServiceException(e);
        }
    }

    /**
     * Pix manager PRPAIN201309UV02 (Query).
     *
     * @param body the body
     * @return the PRPAIN201310UV02 (Query Response)
     */
    @Override
    public PRPAIN201310UV02 pixManagerPRPAIN201309UV02(PRPAIN201309UV02 body) {
        try (PIXManagerService.PIXManagerPortTypeProxy port = createPort()) {
            final PRPAIN201310UV02 pixManagerPRPAIN201309UV02 = port
                    .pixManagerPRPAIN201309UV02(body);
            return pixManagerPRPAIN201309UV02;
        } catch (final Exception e) {
            throw toPixManagerServiceException(e);
        }
    }

    /**
     * To pix manager service exception.
     *
     * @param exception the exception
     * @return the pix manager service exception
     */
    private PixManagerServiceException toPixManagerServiceException(
            Exception exception) {
        log.error("Error closing PixManagerService client port");
        log.error(exception.getMessage(), exception);
        return new PixManagerServiceException(exception);
    }
}
