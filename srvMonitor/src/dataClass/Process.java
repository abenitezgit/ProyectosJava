/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataClass;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author andresbenitez
 */
public class Process {
    String procID;
    int nOrder;
    int critical;
    
    Map<String, Object> params = new HashMap();

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public int getCritical() {
        return critical;
    }

    public void setCritical(int critical) {
        this.critical = critical;
    }

    public int getnOrder() {
        return nOrder;
    }

    public void setnOrder(int nOrder) {
        this.nOrder = nOrder;
    }

    public String getProcID() {
        return procID;
    }

    public void setProcID(String procID) {
        this.procID = procID;
    }
}
