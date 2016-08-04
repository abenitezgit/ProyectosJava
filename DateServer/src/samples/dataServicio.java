/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package samples;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andresbenitez
 */
public class dataServicio {
    String srvID;
    String srvDesc;
    int srvEnable;
    List<dataAssignedProc> listAssignedProc = new ArrayList<>();

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

    public List<dataAssignedProc> getListAssignedProc() {
        return listAssignedProc;
    }

    public void setListAssignedProc(List<dataAssignedProc> listAssignedProc) {
        this.listAssignedProc = listAssignedProc;
    }
    
    
}
