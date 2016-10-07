package gov.samhsa.c2s.common.util;

public class UniqueValueGeneratorException extends RuntimeException {

    public UniqueValueGeneratorException() {
        super();
    }

    public UniqueValueGeneratorException(String message, Throwable cause,
                                         boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UniqueValueGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public UniqueValueGeneratorException(String message) {
        super(message);
    }

    public UniqueValueGeneratorException(Throwable cause) {
        super(cause);
    }
}
