package gov.samhsa.c2s.common.cxf;

import lombok.Data;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Data
public abstract class AbstractEnhancedCxfClient {
    private final QName serviceName;
    private final String endpointAddress;
    private boolean loggingInterceptorsEnabled = false;
    private SoapVersion soapVersion = SoapVersion.SOAP_12;
    private Optional<Long> connectionTimeoutMilliseconds = Optional.empty();
    private Optional<Long> receiveTimeoutMilliseconds = Optional.empty();
    private Optional<HTTPClientPolicy> httpClientPolicy = Optional.empty();
    private Optional<Boolean> mtomEnabled = Optional.empty();
    private Optional<List<Interceptor<? extends Message>>> outInterceptors = Optional.empty();
    private Optional<List<Interceptor<? extends Message>>> inInterceptors = Optional.empty();

    public AbstractEnhancedCxfClient(QName serviceName, String endpointAddress) {
        super();
        this.serviceName = serviceName;
        this.endpointAddress = endpointAddress;
    }

    public void setConnectionTimeoutMilliseconds(Long connectionTimeoutMilliseconds) {
        this.connectionTimeoutMilliseconds = Optional.ofNullable(connectionTimeoutMilliseconds);
    }

    public void setReceiveTimeoutMilliseconds(Long receiveTimeoutMilliseconds) {
        this.receiveTimeoutMilliseconds = Optional.ofNullable(receiveTimeoutMilliseconds);
    }

    public void setHttpClientPolicy(HTTPClientPolicy httpClientPolicy) {
        this.httpClientPolicy = Optional.ofNullable(httpClientPolicy);
    }

    public void setMtomEnabled(Boolean mtomEnabled) {
        this.mtomEnabled = Optional.ofNullable(mtomEnabled);
    }

    public void setOutInterceptors(List<Interceptor<? extends Message>> outInterceptors) {
        this.outInterceptors = Optional.ofNullable(outInterceptors);
    }

    public void setInInterceptors(List<Interceptor<? extends Message>> inInterceptors) {
        this.inInterceptors = Optional.ofNullable(inInterceptors);
    }

    protected abstract <T extends GenericPortTypeProxy> Class<T> getPortTypeClass();

    protected <T extends GenericPortTypeProxy> T createPortProxy() {
        final Service service = Service.create(getServiceName());
        service.addPort(getServiceName(), SoapVersion.SOAP_12.equals(getSoapVersion()) ? SOAPBinding.SOAP12HTTP_BINDING : SOAPBinding.SOAP11HTTP_BINDING, getEndpointAddress());
        final T portTypeProxy = service.getPort(getServiceName(), getPortTypeClass());

        return portTypeProxy;
    }

    protected <T extends GenericPortTypeProxy> T createPort() {
        return configurePort(this::createPortProxy);
    }

    protected <T extends GenericPortTypeProxy> T configurePort(Supplier<T> clientSupplier) {
        final T client = clientSupplier.get();
        CXFLoggingConfigurer.configureInterceptors(client, CXFLoggingConfigurer
                        .serviceNameWithInvokingInstance(client, this),
                isLoggingInterceptorsEnabled());
        setHttpClientConfigIfPresent(client);
        setMtomEnabledIfPresent(client);
        return client;
    }

    private <T extends GenericPortTypeProxy> void setHttpClientConfigIfPresent(T portTypeProxy) {
        final boolean httpClientPolicyRequired = httpClientPolicy.isPresent() || connectionTimeoutMilliseconds.isPresent() || receiveTimeoutMilliseconds.isPresent();
        final boolean interceptorsRequired = this.inInterceptors.isPresent() || this.outInterceptors.isPresent();
        if (httpClientPolicyRequired || interceptorsRequired) {
            final Client client = ClientProxy.getClient(portTypeProxy);
            if (httpClientPolicyRequired) {
                final HTTPConduit http = (HTTPConduit) client.getConduit();
                final HTTPClientPolicy httpClientPolicy = this.httpClientPolicy.orElseGet(HTTPClientPolicy::new);
                connectionTimeoutMilliseconds.ifPresent(httpClientPolicy::setConnectionTimeout);
                receiveTimeoutMilliseconds.ifPresent(httpClientPolicy::setReceiveTimeout);
                http.setClient(httpClientPolicy);
            }
            this.outInterceptors.ifPresent(client.getOutInterceptors()::addAll);
            this.inInterceptors.ifPresent(client.getInInterceptors()::addAll);
        }
    }

    private <T extends GenericPortTypeProxy> void setMtomEnabledIfPresent(T portTypeProxy) {
        this.mtomEnabled.ifPresent(mtomEnabledConfig -> {
            final BindingProvider bp = portTypeProxy;
            SOAPBinding binding = (SOAPBinding) bp.getBinding();
            binding.setMTOMEnabled(mtomEnabledConfig);
        });
    }
}
