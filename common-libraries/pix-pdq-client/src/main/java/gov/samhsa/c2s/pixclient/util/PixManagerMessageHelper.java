package gov.samhsa.c2s.pixclient.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.jxpath.JXPathContext;
import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.MCCIMT000200UV01Acknowledgement;
import org.hl7.v3.MCCIMT000200UV01AcknowledgementDetail;
import org.hl7.v3.MCCIMT000300UV01Acknowledgement;
import org.hl7.v3.MCCIMT000300UV01AcknowledgementDetail;
import org.hl7.v3.PRPAIN201310UV02;
import org.hl7.v3.PRPAMT201307UV02PatientIdentifier;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The Class PixManagerMessageHelper.
 */
@Slf4j
public class PixManagerMessageHelper {

    /**
     * Gets the add and update message.
     *
     * @param response   the response
     * @param pixMgrBean the pix mgr bean
     * @param msg        the msg
     *
     */
    public void getAddUpdateMessage(MCCIIN000002UV01 response,
                                    PixManagerBean pixMgrBean, String msg) {
        log.debug("response ack code:" + response.getAcceptAckCode());
        log.debug("response type id: " + response.getTypeId());
        final List<MCCIMT000200UV01Acknowledgement> ackmntList = response
                .getAcknowledgement();
        for (final MCCIMT000200UV01Acknowledgement ackmnt : ackmntList) {
            if (ackmnt.getTypeCode().getCode().equals(PixPdqConstants.RESPONSE_CA.getMsg())) {
                if (PixPdqConstants.PIX_ADD.getMsg().equals(msg)) {
                    pixMgrBean.setAddMessage(msg + " Success! ");
                } else if (PixPdqConstants.PIX_UPDATE.getMsg().equals(msg)) {
                    pixMgrBean.setUpdateMessage(msg + " Success! ");
                }

                break;
            } else if (ackmnt.getTypeCode().getCode().equals(PixPdqConstants.RESPONSE_CE.getMsg())) {
                final List<MCCIMT000200UV01AcknowledgementDetail> ackmntDetList = ackmnt
                        .getAcknowledgementDetail();
                for (final MCCIMT000200UV01AcknowledgementDetail ackDet : ackmntDetList) {
                    log.error(msg + " error : "
                            + ackDet.getText().toString());
                    if (PixPdqConstants.PIX_ADD.getMsg().equals(msg)) {
                        pixMgrBean.setAddMessage(msg + " Failure! "
                                + ackDet.getText().toString());
                    } else if (PixPdqConstants.PIX_UPDATE.getMsg()
                            .equals(msg)) {
                        pixMgrBean.setUpdateMessage(msg + " Failure! "
                                + ackDet.getText().toString());
                    }
                    break;
                }

            } else {

                if (PixPdqConstants.PIX_ADD.getMsg().equals(msg)) {
                    pixMgrBean.setAddMessage(msg + " Failure! ");
                } else if (PixPdqConstants.PIX_UPDATE.getMsg().equals(msg)) {
                    pixMgrBean.setUpdateMessage(msg + " Failure! ");
                }
            }
        }
    }

    /**
     * Gets the general exp message.
     *
     * @param e  Exception
     * @param pixMgrBean the pix mgr bean
     * @param msg        the msg
     *
     */
    public void getGeneralExpMessage(Exception e, PixManagerBean pixMgrBean,
                                     String msg) {
        // Expect the unexpected
        log.error("Unexpected exception", e);

        // Add error
        log.error("error",
                "Query Failure! Server error! A unexpected error has occured");

        final String errMsg = " Failure! Server error! A unexpected error has occured";
        log.error("exception: " + e.getCause());
        log.error("detail message: " + e.getMessage());

        if (PixPdqConstants.PIX_ADD.getMsg().equals(msg)) {
            pixMgrBean.setAddMessage(msg + errMsg);
        } else if (PixPdqConstants.PIX_QUERY.getMsg().equals(msg)) {
            pixMgrBean.setQueryMessage(msg + errMsg);
        } else if (PixPdqConstants.PIX_UPDATE.getMsg().equals(msg)) {
            pixMgrBean.setUpdateMessage(msg + errMsg);
        }
    }

    /**
     * Gets the query message.
     *
     * @param response   the response
     * @param pixMgrBean the pix mgr bean
     * @return the PixManagerBean
     */
    @SuppressWarnings("unchecked")
    public PixManagerBean getQueryMessage(PRPAIN201310UV02 response,
                                          PixManagerBean pixMgrBean) {

        log.debug("response ack code:" + response.getAcceptAckCode());
        log.debug("response type id: " + response.getTypeId());

        final JXPathContext context = JXPathContext.newContext(response);
        final Iterator<MCCIMT000300UV01Acknowledgement> ackmntList = context
                .iterate("/acknowledgement");

        while (ackmntList.hasNext()) {
            final MCCIMT000300UV01Acknowledgement ackmnt = ackmntList.next();

            if (ackmnt.getTypeCode().getCode().equals(PixPdqConstants.RESPONSE_AA.getMsg())) {
                final StringBuffer queryMsg = new StringBuffer(
                        "Query Success! ");
                final Map<String, String> idMap = new HashMap<String, String>();

                final Iterator<PRPAMT201307UV02PatientIdentifier> pidList = context
                        .iterate("/controlActProcess/queryByParameter/value/parameterList/patientIdentifier");
                while (pidList.hasNext()) {
                    final PRPAMT201307UV02PatientIdentifier pid = pidList
                            .next();
                    final List<II> ptIdList = pid.getValue();

                    for (final II ptId : ptIdList) {
                        queryMsg.append(" Given PID: " + ptId.getExtension());
                        queryMsg.append(" Given UID: " + ptId.getRoot());
                        queryMsg.append("\t");
                    }

                }

                final Iterator<II> ptIdList = context
                        .iterate("/controlActProcess/subject[1]/registrationEvent/subject1[typeCode='SBJ']/patient[classCode='PAT']/id");

                while (ptIdList.hasNext()) {
                    final II pId = ptIdList.next();
                    idMap.put(pId.getRoot(), pId.getExtension());
                }

                pixMgrBean.setQueryMessage(queryMsg.toString());
                pixMgrBean.setQueryIdMap(idMap);
                break;
            } else if (ackmnt.getTypeCode().getCode().equals(PixPdqConstants.RESPONSE_AE.getMsg())) {

                final List<MCCIMT000300UV01AcknowledgementDetail> ackmntDetList = ackmnt
                        .getAcknowledgementDetail();
                for (final MCCIMT000300UV01AcknowledgementDetail ackDet : ackmntDetList) {
                    log.error("Query Failure! "
                            + ackDet.getText().toString());
                    pixMgrBean.setQueryMessage("Query Failure! "
                            + ackDet.getText().toString());
                    pixMgrBean.setQueryIdMap(null);
                    break;
                }

            } else {
                pixMgrBean.setQueryMessage("Query Failure! ");
                pixMgrBean.setQueryIdMap(null);
            }
        }
        return pixMgrBean;
    }

    /**
     * Checks if response is success.
     *
     * @param response the response
     * @return true, if iis success
     */
    public boolean isSuccess(MCCIIN000002UV01 response) {
        return response.getAcknowledgement().stream()
                .map(a -> a.getTypeCode().getCode())
                .filter(c -> PixPdqConstants.RESPONSE_CA.getMsg().equals(c) || PixPdqConstants.RESPONSE_AA.getMsg().equals(c)).count() > 0;
    }
}
