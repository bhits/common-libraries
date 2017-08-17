package gov.samhsa.c2s.pixclient.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PDQSupplierServiceException extends RuntimeException{
    public PDQSupplierServiceException(){}

    public PDQSupplierServiceException(String message) {
        super(message);
    }

    public PDQSupplierServiceException(String message, Throwable cause){
        super(message, cause);
    }

    public PDQSupplierServiceException(Throwable cause) {
        super(cause);
    }

    public PDQSupplierServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
