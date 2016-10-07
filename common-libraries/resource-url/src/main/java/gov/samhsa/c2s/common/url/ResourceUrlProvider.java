package gov.samhsa.c2s.common.url;

import java.net.URL;

public interface ResourceUrlProvider {

    default String getUrl(String packageName, String fileName) {
        final ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();
        final String resourceFile = packageName.replace(".", "/") + "/"
                + fileName;
        final URL resourceUrl = classLoader.getResource(resourceFile);
        final String resourceUrlString = resourceUrl != null ? resourceUrl.toString() : null;
        return resourceUrlString;
    }
}
