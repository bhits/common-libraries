package gov.samhsa.acs.xdsb.repository.wsclient;

import java.net.URL;
import java.util.List;
import java.util.Optional;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import gov.samhsa.acs.common.cxf.AbstractCXFLoggingConfigurerClient;
import ihe.iti.xds_b._2007.DocumentRepositoryService;
import ihe.iti.xds_b._2007.DocumentRepositoryService.DocumentRepositoryPortTypeProxy;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

public class XdsbRepositoryWebServiceClient extends AbstractCXFLoggingConfigurerClient {
	
	private final String endpointAddress;
	
	private final URL wsdlURL = this.getClass().getClassLoader().getResource("wsdl/XDS.b_DocumentRepository.wsdl");
	
	private final QName serviceName = new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Service");
	
	private Optional<List<Interceptor<? extends Message>>> outInterceptors = Optional.empty();

	private Optional<List<Interceptor<? extends Message>>> inInterceptors = Optional.empty();
	
	public XdsbRepositoryWebServiceClient(String endpointAddress) {
		this.endpointAddress = endpointAddress;
	}
	

    public RegistryResponseType documentRepositoryProvideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType body) {
    	try (DocumentRepositoryPortTypeProxy port = createPort()) {
    		final RegistryResponseType response = port.documentRepositoryProvideAndRegisterDocumentSetB(body);
    		return response;
    	} catch (final Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }

    public RetrieveDocumentSetResponseType documentRepositoryRetrieveDocumentSet(RetrieveDocumentSetRequestType body) {
    	try (DocumentRepositoryPortTypeProxy port = createPort()) {
			final RetrieveDocumentSetResponseType response = port.documentRepositoryRetrieveDocumentSet(body);
			return response;
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
    }
    
	private DocumentRepositoryPortTypeProxy createPort() {
		return configurePort(this::createPortProxy);
	}
	
	private DocumentRepositoryPortTypeProxy createPortProxy() {
		final DocumentRepositoryPortTypeProxy port = new DocumentRepositoryService(wsdlURL, serviceName).getDocumentRepositoryPortSoap12();
		if (StringUtils.hasText(this.endpointAddress)) {
			final BindingProvider bp = port;
			bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
			
			SOAPBinding binding = (SOAPBinding) bp.getBinding();
			binding.setMTOMEnabled(true);
		}
		
		this.outInterceptors.ifPresent(ClientProxy.getClient(port)
				.getOutInterceptors()::addAll);
		this.inInterceptors.ifPresent(ClientProxy.getClient(port)
				.getInInterceptors()::addAll);
		
		return port;
	}
	
	
	/**
	 * Sets the in interceptors.
	 *
	 * @param inInterceptors
	 *            the new in interceptors
	 */
	public void setInInterceptors(
			List<Interceptor<? extends Message>> inInterceptors) {
		this.inInterceptors = Optional.of(inInterceptors);
	}

	/**
	 * Sets the out interceptors.
	 *
	 * @param outInterceptors
	 *            the new out interceptors
	 */
	public void setOutInterceptors(
			List<Interceptor<? extends Message>> outInterceptors) {
		this.outInterceptors = Optional.of(outInterceptors);
	}

}
