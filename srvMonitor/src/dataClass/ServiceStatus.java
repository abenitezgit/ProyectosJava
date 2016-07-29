/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataClass;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andresbenitez
 */
public class ServiceStatus {
    String srvID;
    String srvDesc;
    int srvEnable;
    List<AssignedTypeProc> assignedTypeProc = new ArrayList<>();
    
    public ServiceStatus() {
    }
    
    public ServiceStatus(String srvID, String srvDesc, int srvEnable, List<AssignedTypeProc> assignedTypeProc) {
        this.srvID = srvID;
        this.srvDesc = srvDesc;
        this.srvEnable = srvEnable;
        this.assignedTypeProc = assignedTypeProc;
    }

    public String getSrvID() {
        return srvID;
    }

    public void setSrvID(String srvID) {
        this.srvID = srvID;
    }

    public String getSrvDesc() {
        return srvDesc;
    }

    public void setSrvDesc(String srvDesc) {
        this.srvDesc = srvDesc;
    }

    public int getSrvEnable() {
        return srvEnable;
    }

    public void setSrvEnable(int srvEnable) {
        this.srvEnable = srvEnable;
    }

    public List<AssignedTypeProc> getAssignedTypeProc() {
        return assignedTypeProc;
    }

    public void setAssignedTypeProc(List<AssignedTypeProc> assignedTypeProc) {
        this.assignedTypeProc = assignedTypeProc;
    }
    
    
}
