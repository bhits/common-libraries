package gov.samhsa.mhc.common.oauth2;

public class OAuth2AuthenticationException extends RuntimeException{
    public OAuth2AuthenticationException() {
    }

    public OAuth2AuthenticationException(String message) {
        super(message);
    }

    public OAuth2AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public OAuth2AuthenticationException(Throwable cause) {
        super(cause);
    }

    public OAuth2AuthenticationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
