package gov.samhsa.c2s.pixclient.service;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201302UV02;
import org.hl7.v3.PRPAIN201304UV02;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAIN201310UV02;

/**
 * The Interface PixManagerService.
 */
public interface PixManagerService {

    /**
     * Pix manager PRPAIN201301UV02 (Add).
     *
     * @param body the body
     * @return the MCCIIN000002UV01 (Acknowledgement)
     */
    MCCIIN000002UV01 pixManagerPRPAIN201301UV02(
            PRPAIN201301UV02 body);

    /**
     * Pix manager PRPAIN201302UV02 (Update).
     *
     * @param body the body
     * @return the MCCIIN000002UV01 (Acknowledgement)
     */
    MCCIIN000002UV01 pixManagerPRPAIN201302UV02(
            PRPAIN201302UV02 body);

    /**
     * Pix manager PRPAIN201304UV02 (Merge).
     *
     * @param body the body
     * @return the MCCIIN000002UV01 (Acknowledgement)
     */
    MCCIIN000002UV01 pixManagerPRPAIN201304UV02(
            PRPAIN201304UV02 body);

    /**
     * Pix manager PRPAIN201309UV02 (Query).
     *
     * @param body the body
     * @return the PRPAIN201310UV02 (Query Response)
     */
    PRPAIN201310UV02 pixManagerPRPAIN201309UV02(
            PRPAIN201309UV02 body);
}