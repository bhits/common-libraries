package gov.samhsa.c2s.pixclient.service;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.QUQIIN000003UV01Type;

public interface PDQSupplierService {

    public abstract PRPAIN201306UV02 pdqQuery(PRPAIN201305UV02 body);

    public abstract PRPAIN201306UV02 pdqQueryContinue(QUQIIN000003UV01Type body);

    public abstract MCCIIN000002UV01 pdqQueryCancel(QUQIIN000003UV01Type body);
}
