package gov.samhsa.c2s.pixclient.service;

import gov.samhsa.c2s.pixclient.service.exception.PDQSupplierServiceException;
import lombok.extern.slf4j.Slf4j;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.QUQIIN000003UV01Type;
import org.openhie.openpixpdq.services.PDQSupplierService.PDQSupplierPortTypeProxy;
import org.springframework.util.StringUtils;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import java.net.URL;

@Slf4j
public class PDQSupplierServiceImpl implements PDQSupplierService {

    final URL wsdlURL = this.getClass().getClassLoader()
            .getResource("wsdl/PIXPDQManager.wsdl");

    final QName serviceName = new QName("urn:org:openhie:openpixpdq:services", "PDQSupplier_Service");

    private final String endpointAddress;

    public PDQSupplierServiceImpl(String endpointAddress) {
        this.endpointAddress = endpointAddress;
    }

    @Override
    public PRPAIN201306UV02 pdqQuery(PRPAIN201305UV02 body) {
        try (PDQSupplierPortTypeProxy port = createPort()) {
            final PRPAIN201306UV02 pdqSupplierPRPAIN201305UV02 = port.pdqSupplierPRPAIN201305UV02(body);
            return pdqSupplierPRPAIN201305UV02;
        } catch (final Exception e) {
            throw toPDQSupplierServiceException(e);
        }
    }

    @Override
    public PRPAIN201306UV02 pdqQueryContinue(QUQIIN000003UV01Type body) {
        try (PDQSupplierPortTypeProxy port = createPort()) {
            final PRPAIN201306UV02 pdqSupplierQUQIIN000003UV01Continue = port.pdqSupplierQUQIIN000003UV01Continue(body);
            return pdqSupplierQUQIIN000003UV01Continue;
        } catch (final Exception e) {
            throw toPDQSupplierServiceException(e);
        }
    }

    @Override
    public MCCIIN000002UV01 pdqQueryCancel(QUQIIN000003UV01Type body) {
        try (PDQSupplierPortTypeProxy port = createPort()) {
            final MCCIIN000002UV01 pdqSupplierQUQIIN000003UV01Cancel = port.pdqSupplierQUQIIN000003UV01Cancel(body);
            return pdqSupplierQUQIIN000003UV01Cancel;
        } catch (final Exception e) {
            throw toPDQSupplierServiceException(e);
        }
    }

    PDQSupplierPortTypeProxy createPort() {
        return createPortProxy();
    }

    PDQSupplierPortTypeProxy createPortProxy() {
        final PDQSupplierPortTypeProxy port = new org.openhie.openpixpdq.services.PDQSupplierService(wsdlURL, serviceName)
                .getPDQSupplierPortSoap12();
        if (StringUtils.hasText(this.endpointAddress)) {
            final BindingProvider bp = port;
            bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, this.endpointAddress);
        }
        return port;
    }

    private PDQSupplierServiceException toPDQSupplierServiceException(Exception exception) {
        log.error("Error closing PDQSupplierService client port");
        log.error(exception.getMessage(), exception);
        return new PDQSupplierServiceException(exception);
    }
}
