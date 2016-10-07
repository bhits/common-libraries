package gov.samhsa.c2s.common.document.accessor;

import javax.xml.xpath.XPathExpressionException;

public class DocumentAccessorException extends XPathExpressionException {

    public DocumentAccessorException(String message) {
        super(message);
    }

    public DocumentAccessorException(Throwable cause) {
        super(cause);
    }
}
