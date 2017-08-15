package gov.samhsa.c2s.pixclient.util;

public enum PixPdqConstants {
    PIX_ADD("Add"),
    PIX_UPDATE("Update"),
    PIX_QUERY("Query"),
    RESPONSE_AA("AA"),
    RESPONSE_CA("CA"),
    RESPONSE_CE("CE"),
    RESPONSE_AE("AE"),
    RESPONSE_EE("EE");

    private String msg;

    PixPdqConstants(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
