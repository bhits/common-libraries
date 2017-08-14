package gov.samhsa.c2s.pixclient.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PixManagerServiceException extends RuntimeException {
    public PixManagerServiceException() {
    }

    public PixManagerServiceException(String message) {
        super(message);
    }

    public PixManagerServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PixManagerServiceException(Throwable cause) {
        super(cause);
    }

    public PixManagerServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
