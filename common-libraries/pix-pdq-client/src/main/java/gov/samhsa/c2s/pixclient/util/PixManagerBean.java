package gov.samhsa.c2s.pixclient.util;

import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class PixManagerBean {

    private String addMessage = "";
    private String updateMessage = "";
    private String queryMessage = "";
    private Map<String, String> queryIdMap;
    private boolean isSuccess;

    public String getAddMessage() {
        return addMessage;
    }

    public void setAddMessage(String addMessage) {
        this.addMessage = addMessage;
    }

    public String getUpdateMessage() {
        return updateMessage;
    }

    public void setUpdateMessage(String updateMessage) {
        this.updateMessage = updateMessage;
    }

    public String getQueryMessage() {
        return queryMessage;
    }

    public void setQueryMessage(String queryMessage) {
        this.queryMessage = queryMessage;
    }

    public Map<String, String> getQueryIdMap() {
        return queryIdMap;
    }

    public void setQueryIdMap(Map<String, String> queryIdMap) {
        this.queryIdMap = queryIdMap;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
