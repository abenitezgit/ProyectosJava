/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataClass;

import java.util.Date;

/**
 *
 * @author andresbenitez
 */
public class Grupo {
    String grpID;
    String grpDESC;
    String grpCLIID;
    String grpHORID;
    String grpUFechaExec;
    String status;

    public String getGrpUFechaExec() {
        return grpUFechaExec;
    }

    public void setGrpUFechaExec(String grpUFechaExec) {
        this.grpUFechaExec = grpUFechaExec;
    }
    
    public String getGrpDESC() {
        return grpDESC;
    }

    public void setGrpDESC(String grpDESC) {
        this.grpDESC = grpDESC;
    }

    public String getGrpCLIID() {
        return grpCLIID;
    }

    public void setGrpCLIID(String grpCLIID) {
        this.grpCLIID = grpCLIID;
    }

    public String getGrpHORID() {
        return grpHORID;
    }

    public void setGrpHORID(String grpHORID) {
        this.grpHORID = grpHORID;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGrpID() {
        return grpID;
    }

    public void setGrpID(String grpID) {
        this.grpID = grpID;
    }
    
}
