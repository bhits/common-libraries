package gov.samhsa.c2s.common.unit.io;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class SpringBasedResourceFileReader {

    private static final Logger logger = LoggerFactory.getLogger(SpringBasedResourceFileReader.class);
    public static String getStringFromResourceFile(String resourceFileUri) {
        StringWriter writer = new StringWriter();
        ClassPathResource cpr = new ClassPathResource(resourceFileUri);
        try {
            InputStream inputStream = cpr.getInputStream();
            IOUtils.copy(inputStream, writer);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        String result = writer.toString();
        try {
            writer.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return result;
    }

}
