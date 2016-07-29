/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataClass;

/**
 *
 * @author andresbenitez
 */
public class ServiceStatus {
    String srvID;
    int numProcMax;
    int srvEnable;
    boolean srvActive;
    boolean isActivePrimaryMonHost;
    boolean isSocketServerActive;
    boolean isConnectMonHost;
    boolean isRegisterService;
    int numProcExec;
    int numTotalExec;
    String srvStartTime;
    boolean srvLoadParam;

    public String getSrvStartTime() {
        return srvStartTime;
    }

    public void setSrvStartTime(String srvStartTime) {
        this.srvStartTime = srvStartTime;
    }

    public boolean isSrvLoadParam() {
        return srvLoadParam;
    }

    public void setSrvLoadParam(boolean srvLoadParam) {
        this.srvLoadParam = srvLoadParam;
    }

    public boolean isIsActivePrimaryMonHost() {
        return isActivePrimaryMonHost;
    }

    public void setIsActivePrimaryMonHost(boolean isActivePrimaryMonHost) {
        this.isActivePrimaryMonHost = isActivePrimaryMonHost;
    }

    public boolean isIsSocketServerActive() {
        return isSocketServerActive;
    }

    public void setIsSocketServerActive(boolean isSocketServerActive) {
        this.isSocketServerActive = isSocketServerActive;
    }

    public boolean isIsConnectMonHost() {
        return isConnectMonHost;
    }

    public void setIsConnectMonHost(boolean isConnectMonHost) {
        this.isConnectMonHost = isConnectMonHost;
    }

    public boolean isIsRegisterService() {
        return isRegisterService;
    }

    public void setIsRegisterService(boolean isRegisterService) {
        this.isRegisterService = isRegisterService;
    }

    public int getNumProcExec() {
        return numProcExec;
    }

    public void setNumProcExec(int numProcExec) {
        this.numProcExec = numProcExec;
    }

    public int getNumTotalExec() {
        return numTotalExec;
    }

    public void setNumTotalExec(int numTotalExec) {
        this.numTotalExec = numTotalExec;
    }

    public boolean isSrvActive() {
        return srvActive;
    }

    public void setSrvActive(boolean srvActive) {
        this.srvActive = srvActive;
    }

    public int getSrvEnable() {
        return srvEnable;
    }

    public void setSrvEnable(int srvEnable) {
        this.srvEnable = srvEnable;
    }

    public String getSrvID() {
        return srvID;
    }

    public void setSrvID(String srvID) {
        this.srvID = srvID;
    }

    public int getNumProcMax() {
        return numProcMax;
    }

    public void setNumProcMax(int numProcMax) {
        this.numProcMax = numProcMax;
    }
}
