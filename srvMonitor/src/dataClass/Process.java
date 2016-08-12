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
public class Process {
    String procID;
    int nOrder;

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
