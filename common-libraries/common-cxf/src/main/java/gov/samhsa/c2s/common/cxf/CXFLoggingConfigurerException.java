package gov.samhsa.c2s.common.cxf;


public class CXFLoggingConfigurerException extends RuntimeException {
    public CXFLoggingConfigurerException() {
    }

    public CXFLoggingConfigurerException(String message) {
        super(message);
    }

    public CXFLoggingConfigurerException(String message, Throwable cause) {
        super(message, cause);
    }

    public CXFLoggingConfigurerException(Throwable cause) {
        super(cause);
    }

    public CXFLoggingConfigurerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
