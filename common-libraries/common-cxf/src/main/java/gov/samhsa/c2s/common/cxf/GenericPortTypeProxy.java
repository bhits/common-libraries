package gov.samhsa.c2s.common.cxf;

import org.apache.cxf.endpoint.Client;

import javax.xml.ws.BindingProvider;

public interface GenericPortTypeProxy extends AutoCloseable, BindingProvider, Client {
}
