package gov.samhsa.c2s.pixclient.service;

import gov.samhsa.c2s.pixclient.service.exception.PixManagerServiceException;
import lombok.extern.slf4j.Slf4j;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201302UV02;
import org.hl7.v3.PRPAIN201304UV02;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAIN201310UV02;
import org.openhie.openpixpdq.services.PIXManagerService;
import org.springframework.util.StringUtils;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import java.net.URL;

/**
 * The Class PixManagerServiceImpl.
 */
@Slf4j
public class PixManagerServiceImpl implements PixManagerService {


    /**
     * The wsdl url.
     */
    final URL wsdlURL = this.getClass().getClassLoader()
            .getResource("wsdl/PIXManager.wsdl");
    /**
     * The service name.
     */
    final QName serviceName = new QName("urn:ihe:iti:pixv3:2007",
            "PIXManager_Service");
    /**
     * The endpoint address.
     */
    private final String endpointAddress;

    /**
     * Instantiates a new pix manager service impl.
     *
     * @param endpointAddress the endpoint address
     */
    public PixManagerServiceImpl(String endpointAddress) {
        this.endpointAddress = endpointAddress;
    }


    /**
     * Pix manager prpai n201301 u v02.
     *
     * @param body the body
     * @return the MCCII n000002 u v01
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
     * Pix manager prpai n201302 u v02.
     *
     * @param body the body
     * @return the MCCII n000002 u v01
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
     * Pix manager prpai n201304 u v02.
     *
     * @param body the body
     * @return the MCCII n000002 u v01
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
     * Pix manager prpai n201309 u v02.
     *
     * @param body the body
     * @return the PRPAI n201310 u v02
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
     * Creates the port.
     *
     * @return the PIX manager port type proxy
     */
    PIXManagerService.PIXManagerPortTypeProxy createPort() {
        return createPortProxy();
    }

    /**
     * Creates the port proxy.
     *
     * @return the PIX manager port type proxy
     */
    PIXManagerService.PIXManagerPortTypeProxy createPortProxy() {
        final PIXManagerService.PIXManagerPortTypeProxy port = new PIXManagerService(wsdlURL,
                serviceName).getPIXManagerPortSoap12();
        if (StringUtils.hasText(this.endpointAddress)) {
            final BindingProvider bp = port;
            bp.getRequestContext().put(
                    BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                    this.endpointAddress);
        }
        return port;
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
