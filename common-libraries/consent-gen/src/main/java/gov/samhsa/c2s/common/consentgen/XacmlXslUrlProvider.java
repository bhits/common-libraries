package gov.samhsa.c2s.common.consentgen;


import gov.samhsa.c2s.common.url.ResourceUrlProvider;

public interface XacmlXslUrlProvider extends ResourceUrlProvider {
    public abstract String getUrl(XslResource xslResource);
}
