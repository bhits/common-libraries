package gov.samhsa.c2s.common.document.converter;

public class DocumentXmlConverterException extends RuntimeException {

    public DocumentXmlConverterException() {
        super();
    }

    public DocumentXmlConverterException(String message, Throwable cause,
                                         boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DocumentXmlConverterException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentXmlConverterException(String message) {
        super(message);
    }

    public DocumentXmlConverterException(Throwable cause) {
        super(cause);
    }
}
