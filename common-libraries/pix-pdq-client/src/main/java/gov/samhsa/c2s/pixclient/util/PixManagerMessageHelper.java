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
     */
    public void getAddUpdateMessage(MCCIIN000002UV01 response,
                                    PixManagerBean pixMgrBean, String msg) {
        log.debug("Response Ack Code:" + response.getAcceptAckCode());
        log.debug("Response Type Id: " + response.getTypeId());

        final List<MCCIMT000200UV01Acknowledgement> acknowledgementList = response
                .getAcknowledgement();
        for (final MCCIMT000200UV01Acknowledgement acknowledgement : acknowledgementList) {
            if (acknowledgement.getTypeCode().getCode().equals(PixPdqConstants.RESPONSE_CA.getMsg())) {
                if (PixPdqConstants.PIX_ADD.getMsg().equals(msg)) {
                    pixMgrBean.setAddMessage(msg + " Success! ");
                } else if (PixPdqConstants.PIX_UPDATE.getMsg().equals(msg)) {
                    pixMgrBean.setUpdateMessage(msg + " Success! ");
                }
                pixMgrBean.setSuccess(true);

                break;
            } else if (acknowledgement.getTypeCode().getCode().equals(PixPdqConstants.RESPONSE_CE.getMsg())) {
                final List<MCCIMT000200UV01AcknowledgementDetail> acknowledgementDetailList = acknowledgement
                        .getAcknowledgementDetail();
                final MCCIMT000200UV01AcknowledgementDetail acknowledgementDetail = acknowledgementDetailList.get(0);

                log.error(msg + " error : " + acknowledgementDetail.getText().toString());

                if (PixPdqConstants.PIX_ADD.getMsg().equals(msg)) {
                    pixMgrBean.setAddMessage(msg + " Failure! " + acknowledgementDetail.getText().toString());
                } else if (PixPdqConstants.PIX_UPDATE.getMsg()
                        .equals(msg)) {
                    pixMgrBean.setUpdateMessage(msg + " Failure! " + acknowledgementDetail.getText().toString());
                }
                pixMgrBean.setSuccess(false);

            } else {

                if (PixPdqConstants.PIX_ADD.getMsg().equals(msg)) {
                    pixMgrBean.setAddMessage(msg + " Failure! ");
                } else if (PixPdqConstants.PIX_UPDATE.getMsg().equals(msg)) {
                    pixMgrBean.setUpdateMessage(msg + " Failure! ");
                }
                pixMgrBean.setSuccess(false);
            }
        }
    }

    /**
     * Gets the general exp message.
     *
     * @param e          Exception
     * @param pixMgrBean the pix mgr bean
     * @param msg        the msg
     */
    public void getGeneralExpMessage(Exception e, PixManagerBean pixMgrBean,
                                     String msg) {
        // Expect the unexpected
        log.error("Unexpected exception", e);

        final String errMsg = " Failure - An unexpected error has occurred! ";
        log.error("Exception: " + e.getCause());
        log.error("Detail Message: " + e.getMessage());

        if (PixPdqConstants.PIX_ADD.getMsg().equals(msg)) {
            pixMgrBean.setAddMessage(msg + errMsg);
        } else if (PixPdqConstants.PIX_QUERY.getMsg().equals(msg)) {
            pixMgrBean.setQueryMessage(msg + errMsg);
        } else if (PixPdqConstants.PIX_UPDATE.getMsg().equals(msg)) {
            pixMgrBean.setUpdateMessage(msg + errMsg);
        }
    }

    /**
     * Sets the query message.
     *
     * @param response   the response
     * @param pixMgrBean the pix mgr bean
     * @return the PixManagerBean
     */
    @SuppressWarnings("unchecked")
    public PixManagerBean setQueryMessage(PRPAIN201310UV02 response,
                                          PixManagerBean pixMgrBean) {

        log.debug("Response Ack Code:" + response.getAcceptAckCode());
        log.debug("Response Type Id: " + response.getTypeId());

        final JXPathContext context = JXPathContext.newContext(response);
        final Iterator<MCCIMT000300UV01Acknowledgement> acknowledgementList = context
                .iterate("/acknowledgement");

        while (acknowledgementList.hasNext()) {
            final MCCIMT000300UV01Acknowledgement acknowledgement = acknowledgementList.next();

            if (acknowledgement.getTypeCode().getCode().equals(PixPdqConstants.RESPONSE_AA.getMsg())) {
                final Map<String, String> idMap = new HashMap<>();

                final Iterator<II> ptIdList = context
                        .iterate("/controlActProcess/subject[1]/registrationEvent/subject1[typeCode='SBJ']/patient[classCode='PAT']/id");

                while (ptIdList.hasNext()) {
                    final II pId = ptIdList.next();
                    idMap.put(pId.getRoot(), pId.getExtension());
                }

                pixMgrBean.setQueryMessage("Query Success!");
                pixMgrBean.setQueryIdMap(idMap);
                pixMgrBean.setSuccess(true);
                break;
            } else if (acknowledgement.getTypeCode().getCode().equals(PixPdqConstants.RESPONSE_AE.getMsg())) {
                final List<MCCIMT000300UV01AcknowledgementDetail> acknowledgementDetailList = acknowledgement
                        .getAcknowledgementDetail();
                MCCIMT000300UV01AcknowledgementDetail acknowledgementDetail = acknowledgementDetailList.get(0);
                log.error("Query Failure! " + acknowledgementDetail.getText().toString());
                pixMgrBean.setQueryMessage("Query Failure! " + acknowledgementDetail.getText().toString());
                pixMgrBean.setQueryIdMap(null);
                pixMgrBean.setSuccess(false);
            } else {
                pixMgrBean.setQueryMessage("Query Failure! ");
                pixMgrBean.setQueryIdMap(null);
                pixMgrBean.setSuccess(false);
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
