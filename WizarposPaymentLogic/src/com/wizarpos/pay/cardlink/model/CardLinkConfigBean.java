package com.wizarpos.pay.cardlink.model;

import java.io.Serializable;

/**
 * Created by wu on 16/3/28.
 */
public class CardLinkConfigBean implements Serializable{

    /**
     * authCode :
     * mechantId :
     * serverIP :
     * serverPort :
     * terminalId :
     * tpddu :
     */

    private String authCode;
    private String mechantId;
    private String serverIP;
    private String serverPort;
    private String terminalId;
    private String tpdu;

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getMechantId() {
        return mechantId;
    }

    public void setMechantId(String mechantId) {
        this.mechantId = mechantId;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTpdu() {
        return tpdu;
    }

    public void setTpdu(String tpdu) {
        this.tpdu = tpdu;
    }
}
